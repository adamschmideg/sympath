(ns sympath.frontend
  (:use
    [sympath.private :only [from-host-form to-host-form]])
  (:require
    [sympath.core :as core]))

(defn ^:export parse-path
  [x]
  (to-host-form (core/parse-path x)))

