(ns sympath.private)

(defn parse-int
  [s]
  (try
    (Integer/parseInt s)
    (catch NumberFormatException e nil)))

(defn to-host-form
  [s]
  s)

(defn from-host-form
  [s]
  s)
