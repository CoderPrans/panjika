(ns panjika.calc
  (:require ["astronomy-engine" :as astronomy]
            [panjika.const :as const]))

(defn js-parse
  "shallow convert typed js object into maps"
  [x]
  (let [keys (.keys js/Object x)
        vals (.values js/Object x)]
    (->> (map #(list %1 %2) keys vals)
         flatten (apply hash-map)
         clojure.walk/keywordize-keys)))

(defn eclipticCoor [body date]
  (let [ec (-> body
               (astronomy/GeoVector date false)
               astronomy/Ecliptic)]
    {:lon (.-elon ec) :lat (.-elat ec)}))

;; Moon Coordinates
(eclipticCoor "Moon" (js/Date.))
;; => {:lon 299.2528999538679, :lat -4.798447605680219}

;; Sun Coordinates
(eclipticCoor "Sun" (js/Date.))
;; => {:lon 85.39288198079524, :lat -0.002882524875340891}

(defn pair-long [dt]
  (Math/abs (- (:lon (eclipticCoor "Moon" dt))
               (:lon (eclipticCoor "Sun" dt)))))

;; (pair-long (js/Date. "2022-06-21T09:40"))
(let [dt (js/Date. "2022-06-21T09:40")]
  [dt (:lon (eclipticCoor "Moon" dt))
   (:lon (eclipticCoor "Sun" dt))])
;; => [#inst "2022-06-21T04:10:00.000-00:00" 359.99241525432694 89.49395730724663]
;; => [#inst "2022-06-21T04:11:00.000-00:00" 0.001580926007934 89.4946199599948]
;; bit of a pickle. diff drops from 270.4 to 89.5 in 1 min.

;; (astronomy/MoonPhase (js/Date. "2022-06-21T09:41"))
;; => 270.50696096601314

                                        ; Tithi

;; The Ecliptic Longitude of Moon relative to Sun, in 30 segments
(def tithi-long-factor 12)

(defn get-tithi [dt]
  (let [diff-ind (float (/ #_(pair-long dt)
                           (astronomy/MoonPhase dt)
                           tithi-long-factor))]
    (vector (const/tithis diff-ind) (mod diff-ind 1))))

(get-tithi (js/Date.))
;; => ["Krshn Ashtami" 0.14506025311429838]

;; (defn next-tthi []
;;   (let [time-now (js/Date.)
;;         get-diff #(int (float (/ (pair-long %)
;;                                 tithi-long-factor)))
;;         diff-ind (get-diff time-now)
;;         idx (if (< diff-ind 29) (inc diff-ind) 0)]
;;     (loop [td diff-ind
;;            inst (.getTime time-now)]
;;       (if-not (= idx td)
;;         (let [pl (get-diff (js/Date. inst))]
;;           (recur pl (+ (* 1000 60) inst)))
;;         (js/Date. inst)))))

(def ayanmasa 24) ; the angle b/w tropical & sidereal 0deg

                                        ; Nakshatra

;; Ecliptic Longitude of Moon, in 27 segments

(defn get-nakshatra [body dt]
  (let [to-asvini (- (:lon (eclipticCoor body dt))
                     ayanmasa)
        index (/ to-asvini (/ 360 27))]
    (vector
     (const/nakshatras (if (> index 0) index (+ 27 index)))
     (mod index 1))))

(get-nakshatra "Moon" (js/Date.))
;; => ["Krittika" 0.4694790879212025]
                                        ; Rashi

;; Ecliptic Longitude of Moon, in 27 segments

(defn get-rashi [body dt]
  (let [to-mesha (- (:lon (eclipticCoor body dt))
                     ayanmasa)
        index (/ to-mesha 30)]
    (vector
     (const/rashis (if (> index 0) index (+ 12 index)))
     (mod index 1))))

(get-rashi "Sun" (js/Date.))
;; => ["Mithuna (♊︎ Gem.)" 0.32826329039507796]


                                        ; Masa

;; Naskhatra on the next purnima

(defn get-masa [dt]
  (let [nks #(first (get-nakshatra "Moon" %))
        purnima-time #(:date (js-parse (astronomy/SearchMoonPhase % dt 30)))
        next-purnima-entry (purnima-time 180)
        next-purnima-exit (purnima-time 192)
        entry-nks (nks next-purnima-entry)
        exit-nks (nks next-purnima-exit)]
    #_{next-purnima-entry entry-nks
       next-purnima-exit exit-nks}
    (or (const/nks_mas entry-nks)
        (const/nks_mas (const/nakshatras (dec (.indexOf const/nakshatras entry-nks)))))))

(get-masa (js/Date. 2022 5 19))
;; => "Asadha"
;; => {#inst "2022-07-13T18:38:04.131-00:00" "U.Asadha (0.02871)", #inst "2022-07-14T14:47:14.637-00:00" "U.Asadha (0.98874)"}
;; (keys const/nks_mas)
;; (get-nakshatra (:date (js-parse (astronomy/SearchMoonPhase 180 (new js/Date 2022 9 15) 30))))

;; (defn get-masa [dt]
;;   (let [time-now dt
;;         get-diff #(int (float (/ (pair-long %)
;;                                 tithi-long-factor)))
;;         next-purnima (loop [td (get-diff time-now)
;;                             inst (.getTime time-now)]
;;                        (if (not (= td 14))
;;                          (let [tl (get-diff (js/Date. inst))]
;;                            (recur tl (+ (* 1000 60 60 6) inst)))
;;                          (js/Date. inst)))
        
;;         naks-index (.indexOf
;;                     const/nakshatras
;;                     (-> (get-nakshatra next-purnima)
;;                         (clojure.string/split " ")
;;                         first))
;;         mas #(const/nks_mas (get const/nakshatras %))]
;;     #_(mas (- naks-index 1))
;;     (or (mas naks-index)
;;         (mas (inc naks-index)))))

;; => "Asadha"

                                        ; For Date Time
(defn for-dt [dt]
  (hash-map :tithi (get-tithi dt)
            :sun {:naks (get-nakshatra "Sun" dt)
                  :rashi (get-rashi "Sun" dt)}
            :moon {:naks (get-nakshatra "Moon" dt)
                   :rashi (get-rashi "Moon" dt)}
            :masa (get-masa dt)))

(for-dt (new js/Date 2005 7 16 11 26))
;; => {:moon
;;     {:naks ["Mula" 0.7497288071350958],
;;      :rashi ["Dhanu (♐︎ Sag.)" 0.3332128031711541]},
;;     :sun
;;     {:naks ["Aslesha" 0.9550769774396777],
;;      :rashi ["Karka (♋︎ Can.)" 0.9800342121954126]},
;;     :tithi ["Shukl Ekadashi" 0.8829464774393525],
;;     :masa "Shraavana"}

                                        ; EquatorialCoordinate chart

#_(defn get-constellation [bodies]
  "Takes a sequence of bodies, returns their constellations in a map"
  (apply merge
         (map #(let [eq (fn [d] (equatorCoord d (js/Date.)))
                     ra (.-ra (eq %))
                     dec (.-dec (eq %))]
                 (hash-map (keyword %) (.-name (astronomy/Constellation ra dec))))
              bodies)))

#_(get-constellation ["Sun" "Moon"])
;; => {:Sun "Taurus", :Moon "Pisces"}

;; (apply merge
;;        (map #(let [eq (fn [d] (equatorCoord d (js/Date.)))]
;;                (hash-map (keyword %) {:ra (.-ra (eq %)) :dec (.-dec (eq % ))}))
;;             ["Moon" "Sun"]))
;; => {:Sun {:ra 4.225829012729178, :dec 21.182852562375412}, :Moon {:ra 1.6658110006483537, :dec 7.086044700301664}}


(#(let [observer (new astronomy/Observer 23.17 75.78 0)
        {:keys [ra dec]} (js-parse (astronomy/Equator "Sun" % observer true false))
        sun-alt ((js-parse (astronomy/Horizon % observer ra dec false)) :altitude)
        from-east (if (>= (.getHours %) 12)
                    (+ 90 (- 90 sun-alt))
                    (if (>= sun-alt 0) sun-alt (+ 360 sun-alt)))
        rashi (.indexOf const/rashis (first (get-rashi "Sun" %)))
        passed (* 30 (last (get-rashi "Sun" %)))]
    [(inc rashi) (- from-east passed) sun-alt])
  (js/Date.))
;; => [3 210.975843150844 -42.76093408231793]
(.toString (js/Date.))
;; => "Mon Jun 27 2022 23:54:45 GMT+0530 (India Standard Time)"

;; Sun altitude -42deg at midnight ? should be -90


(comment
  (.-elon (astronomy/SunPosition (js/Date.)));; => 65.60813882173356
  (.-lon (astronomy/EclipticGeoMoon (js/Date.)));; => 25.342441575024562
  (.-elongation (astronomy/Elongation "Moon" (js/Date.)));; => 40.32764897036262
  (astronomy/MoonPhase (js/Date.));; => 319.72937858834604

  (* 24 (float (/ 24 360)))

  (let [{:keys [quarter]} (js-parse (astronomy/SearchMoonQuarter (js/Date.)))]
    quarter)
  (js-parse (astronomy/NextMoonQuarter (astronomy/SearchMoonQuarter (js/Date.))))

  (js-parse (astronomy/SearchMoonPhase 180 (js/Date.) 30))

  (apply hash-map (flatten (map #(list %1 %2) [1 2 3] [4 5 6])))

  (get-tithi (js/Date. "2022-06-21T01:49"))

  (let [ujjain (new astronomy/Observer 75.78 23.17 0)
        else (new astronomy/Observer -39 72 0)
        eq #(:ra (js-parse (astronomy/Equator "Sun" (js/Date.) % false false)))]
    [(eq ujjain) (eq else)]);; => [6.075787320319758 6.075816553976948]

  (js-parse (astronomy/Equator "Sun" (js/Date.) (new astronomy/Observer 0 0 0) false false))
  ;; => {:vec #object[Vector [object Object]], :ra 6.075686172510978, :dist 1.0163391999787272, :dec 23.432006601419097}

)
;; => nil
