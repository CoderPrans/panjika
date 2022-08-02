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

(defn rot-idx [count idx]
  (if (>= idx count) (mod idx count) idx))

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

(def ayanaamsa 24) ; the angle b/w tropical & sidereal 0deg

                                        ; Nakshatra

;; Ecliptic Longitude of Moon, in 27 segments

(defn get-nakshatra [body dt]
  (let [to-asvini (- (:lon (eclipticCoor body dt))
                     ayanaamsa)
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
                    ayanaamsa)
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
        moon-time #(:date (js-parse (astronomy/SearchMoonPhase % dt 30)))
        next-purnima (moon-time 180)
        ;; next-amavasya (moon-time 359)
        sun-rasi (first (get-rashi "Sun" next-purnima))]
    (or 
     (const/nks_mas (nks next-purnima))
     (const/nks_mas (const/nakshatras (rot-idx 27 (inc (.indexOf const/nakshatras (nks next-purnima))))))
     (const/nks_mas (const/nakshatras (rot-idx 27 (dec (.indexOf const/nakshatras (nks next-purnima))))))
     (const/masa (rot-idx 12 (.indexOf const/rashis sun-rasi)))
     )
    ))

(get-masa (js/Date.))
(get-masa (js/Date. 2022 9 12 13 22))
(get-masa (js/Date. 1999 2 2 17 22))
(get-masa (js/Date. 1996 4 26 14 22))
(get-masa (js/Date. 2005 7 16 11 26))


(first (get-rashi "Sun" (js/Date.)))
(const/masa (.indexOf const/rashis (first (get-rashi "Sun" (:date (js-parse (astronomy/SearchMoonPhase 359 (js/Date.) 30)))))))

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
;;      :rashi ["Dhanu (Sag.)" 0.3332128031711541]},
;;     :sun
;;     {:naks ["Aslesha" 0.9550769774396777],
;;      :rashi ["Karka (Can.)" 0.9800342121954126]},
;;     :tithi ["Shukl Ekadashi" 0.8829464774393525],
;;     :masa "Shraavana"}



                                        ; Lagna Chart


(defn rashi-to-x [rashi x]
  (const/rashis
   (rot-idx 12 (+ (.indexOf const/rashis rashi) x))))

(defn get-lagna [dt]
  (let [observer (new astronomy/Observer 23.17 75.78 0)
        index (/ (- (.-elon (astronomy/Ecliptic
                             (astronomy/ObserverVector dt observer true)))
                    ayanaamsa) 30)]
    (rashi-to-x (const/rashis (if (> index 0) index (+ 12 index))) 3)
    ))

(get-lagna (js/Date.))
;; => "Makara (Cap.)"

(defn get-rahu-ketu [dt]
  (let [{t :time k :kind} (js-parse (astronomy/SearchMoonNode dt))
        node (first (get-rashi "Moon" ((js-parse t) :date)))
        rk (if (= k 1) ["Rahu" "Ketu"] ["Ketu" "Rahu"])]
    (apply hash-map
           [node (rk 0) (rashi-to-x node 6) (rk 1)])))

(get-rahu-ketu (js/Date.))
;; => {"Mesha (Ari.)" "Rahu", "Tula (Lib.)" "Ketu"}

(def grahas ["Moon" "Sun" "Mercury" "Venus" "Mars" "Jupiter" "Saturn"])

(defn dasha [dt]
  (->> (reduce
        #(let [rs (first (get-rashi %2 dt))]
           (assoc %1 rs 
                  (if (%1 rs) (conj (%1 rs) %2 ) (vector %2))))
        {} grahas)
       (merge-with #(flatten (conj %1 %2)) (zipmap const/rashis (repeat [])))
       (merge-with cons (get-rahu-ketu dt))))

(dasha (js/Date.))
;; => {"Mithuna (Gem.)" ("Venus"),
;;     "Vrischika (Sco.)" [],
;;     "Dhanu (Sag.)" [],
;;     "Kanya (Vir.)" [],
;;     "Kumbha (Aqu.)" [],
;;     "Vrishabha (Tau.)" ("Moon"),
;;     "Simha (Leo)" [],
;;     "Makara (Cap.)" ("Saturn"),
;;     "Karka (Can.)" ("Sun" "Mercury"),
;;     "Meen (Pic.)" ("Jupiter"),
;;     "Mesha (Ari.)" ("Rahu" "Mars"),
;;     "Tula (Lib.)" ("Ketu")}


(defn lagna-chart [dt]
  (map #(let [rs (const/rashis
                  (rot-idx
                   (count const/rashis)
                   (+ % (.indexOf const/rashis
                                  (get-lagna dt)))))]
          (list (inc (.indexOf const/rashis rs)) ((dasha dt) rs))) (range 12)))

(lagna-chart (js/Date.))
;; => ((2 [])
;;     (3 ("Venus"))
;;     (4 ("Moon" "Sun" "Mercury"))
;;     (5 [])
;;     (6 [])
;;     (7 ("Ketu"))
;;     (8 [])
;;     (9 [])
;;     (10 ("Saturn"))
;;     (11 [])
;;     (12 ("Jupiter"))
;;     (1 ("Rahu" "Mars")))

;; (lagna-chart (new js/Date 2005 7 16 11 26))
;; (lagna-chart (new js/Date 1999 2 2 17 15))

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

  (+ 5.5 (astronomy/SiderealTime (js/Date.)))
  ;; => 18.865479635990095

  ;; https://astronomy.stackexchange.com/questions/36227/how-to-tell-which-of-the-ecliptic-cross-horizon-angles-is-to-the-east
  ;; ecliptic cross horizon angles?
  ;; tanL = -cosT/(sinEtanP+cosEsinT)
  ;; where, L is (cross horizon longitudes)
  ;; T Local Sidereal Time
  ;; E Obliquity of the ecliptic 23.4
  ;; P Latitude of Observer

  (#(let [pi Math/PI
          t (+ 5.5 (astronomy/SiderealTime %))
          e (* (/ Math/PI 180) 23.4)
          p (* (/ Math/PI 180) 23.17)]
      (Math/atan
       (/ (* -1 (Math/cos t))
          (+ (* (Math/sin e) (Math/tan p))
             (* (Math/cos e) (Math/sin t))))))
   (new js/Date 2005 7 16 12 06))
  ;; => 0.3690437363241621


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


  )
;; => nil
