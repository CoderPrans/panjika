(ns panjika.calc
  (:require ["astronomy-engine" :as astronomy]
            [panjika.const :as const]))

(defn jkeys [x] (.keys js/Object x))

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

(pair-long (js/Date.))
;; => 213.8592086369461

                                        ; Tithi

;; The Ecliptic Longitude of Moon relative to Sun, in 30 segments
(def tithi-long-factor 12)

(defn get-tithi [dt]
  (let [diff-ind (float (/ (pair-long dt)
                           tithi-long-factor))]
    (str (const/tithis diff-ind)
         " ("
         (-> diff-ind (mod 1) (.toFixed 5))
         ")")))

(get-tithi (js/Date.))
;; => "Krshn Tritiya (0.85930)"

(defn next-tthi []
  (let [time-now (js/Date.)
        get-diff #(int (float (/ (pair-long %)
                                tithi-long-factor)))
        diff-ind (get-diff time-now)
        idx (if (< diff-ind 29) (inc diff-ind) 0)]
    (loop [td diff-ind
           inst (.getTime time-now)]
      (if-not (= idx td)
        (let [pl (get-diff (js/Date. inst))]
          (recur pl (+ (* 1000 60) inst)))
        (js/Date. inst)))))

;; (defn to-tithi [idx diff dt]
;;   (loop [td diff
;;          inst (.getTime dt)]
;;     (if (not (= (int td) idx))
;;       (let [tl (t-long (js/Date. inst))]
;;         (recur tl (+ (* 1000 60) inst)))
;;       (js/Date. inst))))

(def ayanmasa 24) ; the angle b/w tropical & sidereal 0deg

                                        ; Nakshatra

;; Ecliptic Longitude of Moon, in 27 segments

(defn get-nakshatra [dt]
  (let [to-asvini (- (:lon (eclipticCoor "Moon" dt))
                     ayanmasa)
        index (/ to-asvini (/ 360 27))]
    (str (const/nakshatras (if (> index 0) index (+ 27 index)))
         " (" (-> index (mod 1) (.toFixed 5)) ")")))

(get-nakshatra (js/Date.))
;; => "U.Asadha (0.67065)"
                                        ; Rashi

;; Ecliptic Longitude of Moon, in 27 segments

(defn get-rashi [dt]
  (let [to-mesha (- (:lon (eclipticCoor "Moon" dt))
                     ayanmasa)
        index (/ to-mesha 30)]
    (str (const/rashis index)
         " (" (-> index (mod 1) (.toFixed 5)) ")")))

(get-rashi (js/Date.))
;; => "Dhanu (0.97724)"


                                        ; Masa

;; Naskhatra on the next purnima

(defn get-masa []
  (let [time-now (js/Date.)
        get-diff #(int (float (/ (pair-long %)
                                tithi-long-factor)))
        next-purnima (loop [td (get-diff time-now)
                            inst (.getTime time-now)]
                       (if (not (= td 14))
                         (let [tl (get-diff (js/Date. inst))]
                           (recur tl (+ (* 1000 60 60) inst)))
                         (js/Date. inst)))
        
        naks-index (.indexOf
                    const/nakshatras
                    (-> (get-nakshatra next-purnima)
                        (clojure.string/split " ")
                        first))
        mas #(const/nks_mas (get const/nakshatras %))]
    #_(mas (- naks-index 1))
    (or (mas naks-index)
        (mas (inc naks-index)))))

(get-masa)
;; => "Asadha"

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

(comment
  (.-elon (astronomy/SunPosition (js/Date.)));; => 65.60813882173356
  (.-lon (astronomy/EclipticGeoMoon (js/Date.)));; => 25.342441575024562
  (.-elongation (astronomy/Elongation "Moon" (js/Date.)));; => 40.32764897036262
  (astronomy/MoonPhase (js/Date.));; => 319.72937858834604

  (* 24 (float (/ 24 360)))


)
