(ns common.impl)

(defn parse-int
  [s]
  (try
    (Integer/parseInt s)
    (catch NumberFormatException e nil)))

(defn to-host-form
  [s]
  s)
