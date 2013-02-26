(ns sympath.private)

(defn parse-int
  [s]
  (let [n (js/parseInt s)]
    ; beware: parseInt("3.5") => 3
    (if (= s (str n)) n s)))

(defn- clj->js
  "Recursively transforms ClojureScript maps into Javascript objects,
  other ClojureScript colls into JavaScript arrays, and ClojureScript
  keywords into JavaScript strings."
  [x]
  (cond
    (string? x) x
    (keyword? x) (name x)
    (map? x) (.-strobj (reduce (fn [m [k v]]
    (assoc m (clj->js k) (clj->js v))) {} x))
    (coll? x) (apply array (map clj->js x))
    :else x))

(defn to-host-form [x] (clj->js x))
