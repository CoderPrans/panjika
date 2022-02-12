(ns panjika.calc
  (:require ["astronomy-engine" :as astronomy]
            [panjika.const :as const]))

(defn jkeys [x] (.keys js/Object x))

;; Ecliptic Longitude of Moon
(defn moon-long [dt]
  (-> "Moon"
      (astronomy/GeoVector dt false)
      astronomy/Ecliptic
      .-elon))

                                        ; Tithi


;; The Ecliptic Longitude of Moon relative to Sun, in 30 segments

(defn get-tithi [dt]
  (let [diff (/ (astronomy/PairLongitude "Moon" "Sun" dt) 12)]
    (str (const/tithis diff) " (" diff ")")))

(get-tithi (js/Date.))
;; => "Ekadashi (11.143252424151465)"

                                        ; Nakshatra


;; Ecliptic Longitude of Moon, in 27 segments

(defn get-nakshatra [dt]
  (let [to-asvini (- (moon-long dt) 30)
        index (/ to-asvini (/ 360 27))]
    (str (const/nakshatras index) " (" index ")")))

(get-nakshatra (js/Date.))
;; => "Ardra (5.098369419897654)"

                                        ; Rashi

;; Ecliptic Longitude of Moon, in 27 segments

(defn get-rashi [dt]
  (let [index (/ (moon-long dt) 30)]
    (str (const/rashis index) " (" index ")")))

(get-rashi (js/Date.))
;; => "Mithuna (3.2675021473810753)"
