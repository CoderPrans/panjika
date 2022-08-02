(ns panjika.const)

(def tithis [
             "Shukl Pratipada (Bright 1)", "Shukl Dwitiya (Bright 2)", "Shukl Tritiya (Bright 3)", "Shukl Chaturthi (Bright 4)", "Shukl Panchami (Bright 5)", "Shukl Shashthi (Bright 6)", "Shukl Saptami (Bright 7)", "Shukl Ashtami (Bright 8)", "Shukl Navami (Bright 9)", "Shukl Dashami (Bright 10)", "Shukl Ekadashi (Bright 11)", "Shukl Dwadashi (Bright 12)", "Shukl Trayodashi (Bright 13)", "Shukl Chaturdashi (Bright 14)",
             "Purnima (Full Moon)",
             "Krshn Pratipada (Dark 1)", "Krshn Dwitiya (Dark 2)", "Krshn Tritiya (Dark 3)", "Krshn Chaturthi (Dark 4)", "Krshn Panchami (Dark 5)", "Krshn Shashthi (Dark 6)", "Krshn Saptami (Dark 7)", "Krshn Ashtami (Dark 8)", "Krshn Navami (Dark 9)", "Krshn Dashmi (Dark 10)", "Krshn Ekadashi (Dark 11)", "Krshn Dwadashi (Dark 12)", "Krshn Trayodashi (Dark 13)", "Krshn Chaturdashi (Dark 14)",
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
