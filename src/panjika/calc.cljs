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
        (recur tl (+ (* 1000 60) inst)))
      (js/Date. inst))))

(defn next-tithi [dt]
  (let [diff (t-long dt)
        diff-int (int diff)]
    (to-tithi
     (if (< diff-int 29) (inc diff-int) 0)
     diff dt)))

(next-tithi (js/Date.))
                                        ; Tithi

;; The Ecliptic Longitude of Moon relative to Sun, in 30 segments


(defn get-tithi [dt]
  (let [diff (t-long dt)]
    (str #_(if (> diff 14) "Krshn" "Shukl")
         (cond (= (int diff) 14) "" ;; Purnima
               (= (int diff) 29) "" ;; Amavasya
               (> diff 14) "Krshn" :else "Shukl")
         " "
         (const/tithis diff) " (" (-> diff (mod 1) (.toFixed 5)) ")")))


(get-tithi (js/Date.))
;; => "Krshn Panchami (19.11211)"

                                        ; Nakshatra

;; Ecliptic Longitude of Moon, in 27 segments

(defn get-nakshatra [dt]
  (let [to-asvini (- (moon-long dt) 30)
        index (/ to-asvini (/ 360 27))]
    (str (const/nakshatras (if (> index 0) index (+ 27 index)))
         " (" (-> index (mod 1) (.toFixed 5)) ")")))

(get-nakshatra (js/Date.))
;; => "U.Bhadrapada (-1.54381)"

                                        ; Masa

;; Naskhatra on the next purnima

(defn get-masa []
  (let [next-purnima (loop [td (t-long (js/Date.))
                            inst (.getTime (js/Date.))]
                       (if (not (= (int td) 14))
                         (let [tl (t-long (js/Date. inst))]
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
;; => "Phalgun"

                                        ; Rashi

;; Ecliptic Longitude of Moon, in 27 segments

(defn get-rashi [dt]
  (let [index (/ (moon-long dt) 30)]
    (str (const/rashis index) " (" (-> index (mod 1) (.toFixed 5)) ")")))

(get-rashi (js/Date.))
;; => "Kanya (6.70434)"


(comment
(let [next-purnima (loop [td (t-long (js/Date.))
                            inst (.getTime (js/Date.))]
                       (if (not (= (int td) 14))
                         (let [tl (t-long (js/Date. inst))]
                           (recur tl (+ (* 1000 60 60) inst)))
                         (js/Date. inst)))
        
        naks-index (.indexOf
                    const/nakshatras
                    (-> (get-nakshatra next-purnima)
                        (clojure.string/split " ")
                        first))
        mas #(const/nks_mas (get const/nakshatras %))]
  (str
       (get const/nakshatras (- naks-index 1)) " "
       (get const/nakshatras naks-index) " "
       (get const/nakshatras (+ naks-index 1))))


)
