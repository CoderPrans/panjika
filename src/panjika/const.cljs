(ns panjika.const)

(def tithis [
             "Shukl Prti. (Br 1)", "Shukl Dwit. (Br 2)", "Shukl Trit. (Br 3)", "Shukl Catu. (Br 4)", "Shukl Panch. (Br 5)", "Shukl Sast. (Br 6)", "Shukl Sapt. (Br 7)", "Shukl Asht. (Br 8)", "Shukl Nava. (Br 9)", "Shukl Dasm. (Br 10)", "Shukl Ekad. (Br 11)", "Shukl Dwad. (Br 12)", "Shukl Tryod. (Br 13)", "Shukl Caturd. (Br 14)",
             "Purnima (Full Moon)",
             "Krshn Prti. (Da 1)", "Krshn Dwit. (Da 2)", "Krshn Trit. (Da 3)", "Krshn Catu. (Da 4)", "Krshn Panch. (Da 5)", "Krshn Sast. (Da 6)", "Krshn Sapt. (Da 7)", "Krshn Asht. (Da 8)", "Krshn Nava. (Da 9)", "Krshn Dasm. (Da 10)", "Krshn Ekad. (Da 11)", "Krshn Dwad. (Da 12)", "Krshn Tryod. (Da 13)", "Krshn Caturd. (Da 14)",
             "Amavasya (New Moon)"
             ])

(def nakshatras ["Ashwini", "Bharini", "Krittika", "Rohini", "Mrigashirasa", "Ardra", "Punarvasu", "Pushya", "Aslesha", "Magha", "P.Phalguni", "U.Phalguni", "Hasta", "Chitra", "Svati", "Visakha", "Anuradha", "Jyestha", "Moola", "P.Asadha", "U.Asadha", "Sravana", "Dhanistha", "Satabhisha", "P.Bhadrapada", "U.Bhadrapada", "Revati"])

(def rashis ["Mesha (Ari.)", "Vrishabha (Tau.)", "Mithuna (Gem.)", "Karka (Can.)", "Simha (Leo)", "Kanya (Vir.)", "Tula (Lib.)", "Vrischika (Sco.)", "Dhanu (Sag.)", "Makara (Cap.)", "Kumbha (Aqu.)", "Meen (Pic.)"])

(def nks_mas {"Chitra"         "Chaitra"
              "Visakha"        "Vaisākha"
              "Jyestha"        "Jyeshta"
              "P.Asadha"       "Āshāda"
              "Sravana"        "Shrāvana"
              "P.Bhadrapada"   "Bhādra"
              "Ashwini"        "Ashwina"
              "Krittika"       "Kartika"
              "Mrigashirasa"   "Mārgasirsa"
              "Pushya"         "Pausha"
              "Magha"          "Māgha"
              "P.Phalguni"     "Phālguna"})

(def masa ["Chaitra"
           "Vaisākha"
           "Jyeshta"
           "Āshāda"
           "Shrāvana"
           "Bhādra"
           "Ashwina"
           "Kartika"
           "Mārgasirsa"
           "Pausha"
           "Māgha"
           "Phālguna"])

;; specific to this apps' lagna representation.
(def house_box [2 1 5 10 15 21 22 23 19 14 9 3])

(def yoga ["Vishkumbha"
           "Priti"
           "Ayushman"
           "Saubhagya"
           "Shobhana"
           "Atiganda"
           "Sukarma"
           "Dhriti"
           "Shoola"
           "Ganda"
           "Vriddhi"
           "Dhruva"
           "Vyaghata"
           "Harshana"
           "Vajra"
           "Siddhi"
           "Vyatipata"
           "Variyana"
           "Parigha"
           "Shiva"
           "Siddha"
           "Sadhya"
           "Shubha"
           "Shukla"
           "Brahma"
           "Indra"
           "Vaidhriti"])

(def karana ["Bava"
             "Balava"
             "Kaulava"
             "Taitila"
             "Gara"
             "Vanija"
             "Vishti"
             "Shakuni"
             "Chatushpada"
             "Naga"
             "Kinstughna"])

(def karana-to-tithi
  (vec (map #(let [[x y] %] (vector (karana x) (karana y)))
               [[10 0] [1 2] [3 4] [5 6] [0 1]
                [2 3] [4 5] [6 0] [1 2] [3 4]
                [5 6] [0 1] [2 3] [4 5] [6 0]
                [1 2] [3 4] [5 6] [0 1] [2 3]
                [4 5] [6 0] [1 2] [3 4] [5 6]
                [0 1] [2 3] [4 5] [6 7] [8 9]])))


(defn ra [h m s]
  (+
   (* h 15)
   (* (/ m 60) 15)
   (* (/ s 3600))))

(defn dc [d m s]
  (+
   d
   (* m (/ 1 60))
   (* s (/ 1 3600))))


;; nakshatras coordinates
(def nks
  [
   ;; Ashwini
   ;; b-Ari
   [(ra 1 54 39.21) (dc 20 48 28.4)]
   ;; a-Ari
   [(ra 2 7 11.26) (dc 23 27 42.8)]
   
   ;; Bharani
   ;; 35-Ari
   [(ra 2 43 27.57) (dc 27 42 23.4)]
   ;; c-Ari
   [(ra 2 49 59.55) (dc 27 15 33.1)]
   
   ;; Krittika
   ;; n-Tau
   [(ra 3 47 29.14) (dc 24 6 16.5)]
   
   ;; Rohini
   ;; a-Tau
   [(ra 4 35 55.11) (dc 16 30 30.5)]
   
   ;; Mrigashira
   ;; l-Ori
   [(ra 5 35 7.73) (dc 9 56 6.9)]
   
   ;; Ardra
   ;; a-Ori
   [(ra 5 55 9.71) (dc 7 24 30.6)]
   
   ;; Punarvasu
   ;; a-Gem
   [(ra 7 34 34.32) (dc 31 53 13.7)]
   ;; b-Gen
   [(ra 7 45 16.65) (dc 28 1 32)]
   
   ;; Pushya
   ;; d-Cnc
   [(ra 8 44 39.81) (dc 18 9 15)]
   
   ;; Aslesha
   ;; e-Hya
   [(ra 8 46 44.93) (dc 6 25 13.1)]
   
   ;; Magha
   ;; a-Leo
   [(ra 10 8 20.62) (dc 11 58 10.3)]
   
   ;; Purva Phalguni
   ;; d-Leo
   [(ra 11 14 5.39) (dc 20 31 32.6)]
   
   ;; Uttara Phalguni
   ;; b-Leo
   [(ra 11 49 1.65) (dc 14 34 25.7)]
   
   ;; Hasta
   ;; d-Crv
   [(ra 12 29 50.62) (dc -16 30 54.3)]
   
   ;; Chitra
   ;; a-Vir
   [(ra 13 25 10.73) (dc -11 9 37.6)]
   
   ;; Swati
   ;; a-Boo
   [(ra 14 15 37.55) (dc 19 10 19.2)]
   
   ;; Visakha
   ;; a2-Lib
   [(ra 14 50 52.18) (dc -16 2 30.2)]
   
   ;; Anuradha
   ;; d-Sco
   [(ra 16 0 20.02) (dc -22 37 20.1)]
   
   ;; Jyestha
   ;; a-Sco
   [(ra 16 29 24.66) (dc -26 25 57.8)]
   
   ;; Moola
   ;;l-Sco
   [(ra 17 33 37.19) (dc -37 6 19.3)]
   
   ;; Purva Ashadha
   ;; d-Sgr
   [(ra 18 21 0.55) (dc -29 49 43.4)]
   ;; e-Sgr
   [(ra 18 24 11.07) (dc -34 23 9.5)]
   
   ;; Uttar Ashadha
   ;; s-Sgr
   [(ra 18 55 16.94) (dc -26 17 49.2)]
   ;; t-Sgr
   [(ra 19 2 37.75) (dc -29 52 48.6)]
   
   ;; Shravana
   ;; a-Aql
   [(ra 19 50 48.93) (dc 8 52 23.3)]
   
   ;; Dhanishta
   ;; b-Del
   [(ra 20 37 34.37) (dc 14 35 49.6)]
   
   ;; Satabisha
   ;; l-Aqr
   [(ra 22 52 38.13) (dc -7 34 38.5)]
   
   ;; Purva Bhadrapada
   ;; b-Peg
   [(ra 23 3 48.13) (dc 28 5 5.1)]
   ;; a-Peg
   [(ra 23 4 46.97) (dc 15 12 23.8)]
   
   ;; Uttara Bhadrapada
   ;; a-And
   [(ra 0 8 24.7) (dc 29 5 22.9)]
   ;; g-Peg
   [(ra 0 13 15.24) (dc 15 11 4.8)]
   
   ;; Revati
   ;; e-Psc
   [(ra 1 2 57.36) (dc 7 53 30.3)]
   
   ])
