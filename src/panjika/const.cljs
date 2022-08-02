(ns panjika.const)

(def tithis [
             "Shukl Prti. (Br 1)", "Shukl Dwit. (Br 2)", "Shukl Trit. (Br 3)", "Shukl Catu. (Br 4)", "Shukl Panch. (Br 5)", "Shukl Sast. (Br 6)", "Shukl Sapt. (Br 7)", "Shukl Asht. (Br 8)", "Shukl Nava. (Br 9)", "Shukl Dasm. (Br 10)", "Shukl Ekad. (Br 11)", "Shukl Dwad. (Br 12)", "Shukl Tryod. (Br 13)", "Shukl Caturd. (Br 14)",
             "Purnima (Full Moon)",
             "Krshn Prti. (Da 1)", "Krshn Dwit. (Da 2)", "Krshn Trit. (Da 3)", "Krshn Catu. (Da 4)", "Krshn Panch. (Da 5)", "Krshn Sast. (Da 6)", "Krshn Sapt. (Da 7)", "Krshn Asht. (Da 8)", "Krshn Nava. (Da 9)", "Krshn Dasm. (Da 10)", "Krshn Ekad. (Da 11)", "Krshn Dwad. (Da 12)", "Krshn Tryod. (Da 13)", "Krshn Caturd. (Da 14)",
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
