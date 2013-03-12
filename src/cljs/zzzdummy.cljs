(ns zzzdummy
  ;; XXX: to make the compiler aware of crossover namespace under clj folder
  ;; I renamed it to start with zzz so its `require` clauses come last.
  ;; Otherwise `frontend` was required before it was provided in the
  ;; generated main-debug.js
  (:require
    [sympath.core :as core]
    [sympath.demo :as demo]
    [sympath.frontend :as frontend]))
