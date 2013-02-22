(ns tree.core
  (:use
    [clojure.string :only [split]]
    [common.impl :only [parse-int to-host-form]]))

(defn debug
  [x]
  (println x)
  x)

(defn- parse-primitive
  [s]
  (condp = s
    "true" true
    "false" false
    "" nil
    (if-let [n (parse-int s)] n s)))

(defn- parse-dictionary-string
  [s]
  (if (re-find #"=" s)
    (->> (split s #",")
      (reduce
        (fn [memo x]
          (let [[k v] (split x #"=")]
            (assoc memo (keyword k) (parse-primitive v))))
        {}))
    (if (= "*" s)
      {}
      (parse-primitive s))))

(defn- parse-xxx
  [s parse-item]
  (map
    #(let [it (parse-item %1)]
      (if (string? it)
        (keyword it)
        it))
    (split s #"/")))

(defn ^:export parse-path
  [s]
  (to-host-form
    (parse-xxx s parse-primitive)))

(defn ^:export parse-selector
  [s]
  (to-host-form
    (parse-xxx s parse-dictionary-string)))

(defn ^:export add [x y]
  (+ x y))

(defn- match-selector
  "Checks if a selector matches the node in `form` denoted by `path`."
  [form path selector]
  (let [path-vec (parse-path path)
        selector-vec (parse-selector selector)
        prepend-len
          (if (re-find #"^/" selector)
            0
            (- (count path-vec) (count selector-vec)))
        absolute-selector
          (concat
            (take (max 0 prepend-len) path-vec)
            selector-vec)]
    ))
