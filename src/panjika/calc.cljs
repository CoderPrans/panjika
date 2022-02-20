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

(defn t-long [dt]
  (/ (astronomy/PairLongitude "Moon" "Sun" dt) 12))

(defn to-tithi [idx diff dt]
  (loop [td diff
         inst (.getTime dt)]
    (if (not (= (int td) idx))
      (let [tl (t-long (js/Date. inst))]
        (recur tl (+ 1000 inst)))
      (js/Date. inst))))

(defn next-tithi [dt]
  (let [diff (t-long dt)]
    (to-tithi (inc (int diff)) diff dt)))

                                        ; Tithi

;; The Ecliptic Longitude of Moon relative to Sun, in 30 segments


(defn get-tithi [dt]
  (let [diff (t-long dt)]
    (str (if (> diff 14) "Krshn" "Shukl") " "
         (const/tithis diff) " (" (.toFixed diff 5) ")")))


(get-tithi (js/Date.))
;; => "Krshn Panchami (19.11211)"

                                        ; Nakshatra

;; Ecliptic Longitude of Moon, in 27 segments

(defn get-nakshatra [dt]
  (let [to-asvini (- (moon-long dt) 30)
        index (/ to-asvini (/ 360 27))]
    (str (const/nakshatras index) " (" (.toFixed index 5) ")")))

(get-nakshatra (js/Date.))
;; => "Hastra (12.83434)"

                                        ; Masa

;; Naskhatra on the next purnima

(defn get-masa []
  (let [next-purnima (loop [td (t-long (js/Date.))
                            inst (.getTime (js/Date.))]
                       (if (not (= (int td) 14))
                         (let [tl (t-long (js/Date. inst))]
                           (recur tl (+ (* 1000 60 60 24) inst)))
                         (js/Date. inst)))
        
        naks-index (.indexOf
                    const/nakshatras
                    (-> (get-nakshatra next-purnima)
                        (clojure.string/split " ")
                        first))
        mas #(const/nks_mas (get const/nakshatras %))]
    (or (mas naks-index)
        (mas (inc naks-index))
        (mas (dec naks-index)))))

(get-masa)
;; => "Phalgun"

                                        ; Rashi

;; Ecliptic Longitude of Moon, in 27 segments

(defn get-rashi [dt]
  (let [index (/ (moon-long dt) 30)]
    (str (const/rashis index) " (" (.toFixed index 5) ")")))

(get-rashi (js/Date.))
;; => "Kanya (6.70434)"


(comment

  )
