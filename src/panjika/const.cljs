(ns panjika.const)

(def tithis [
             "Shukl Pratipada (Br 1)", "Shukl Dwitiya (Br 2)", "Shukl Tritiya (Br 3)", "Shukl Chaturthi (Br 4)", "Shukl Panchami (Br 5)", "Shukl Shashthi (Br 6)", "Shukl Saptami (Br 7)", "Shukl Ashtami (Br 8)", "Shukl Navami (Br 9)", "Shukl Dashami (Br 10)", "Shukl Ekadashi (Br 11)", "Shukl Dwadashi (Br 12)", "Shukl Trayodashi (Br 13)", "Shukl Chaturdashi (Br 14)",
             "Purnima (Full Moon)",
             "Krshn Pratipada (Da 1)", "Krshn Dwitiya (Da 2)", "Krshn Tritiya (Da 3)", "Krshn Chaturthi (Da 4)", "Krshn Panchami (Da 5)", "Krshn Shashthi (Da 6)", "Krshn Saptami (Da 7)", "Krshn Ashtami (Da 8)", "Krshn Navami (Da 9)", "Krshn Dashmi (Da 10)", "Krshn Ekadashi (Da 11)", "Krshn Dwadashi (Da 12)", "Krshn Trayodashi (Da 13)", "Krshn Chaturdashi (Da 14)",
             "Amavasya (New Moon)"
             ])

(def nakshatras ["Ashwini", "Bharini", "Krittika", "Rohini", "Mrigashirasa", "Ardra", "Punarvasu", "Pushya", "Aslesha", "Magha", "P.Phalguni", "U.Phalguni", "Hastra", "Chitra", "Svati", "Visakha", "Anuradha", "Jyestha", "Mula", "P.Asadha", "U.Asadha", "Sravana", "Dhanistha", "Satabhisha", "P.Bhadrapada", "U.Bhadrapada", "Revati"])

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
