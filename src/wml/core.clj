(ns wml.core
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

(def prices
  (with-open [reader (io/reader "resources/data/prices.csv")]
  (doall
    (csv/read-csv reader))))

(defn nth-line [n]
  (with-open [rdr (io/reader "resources/data/2001-09-01/ee62f687-4ed4-d4bd-40d9-c2671edb2768")]
    (nth (line-seq rdr) n)))

(defn void? [s]
  (clojure.string/includes? s "VOID"))

(defn product-code [s]
  (re-find #"\d+" s))

(defn price [s]
  (re-find #"\d+\.\d+" s))

(defn id-match? [s line]
  (clojure.string/includes? s (product-code (nth-line line))))

(defn get-price [line]
  (first (remove nil? (map #(if (id-match? % line) (last %)) prices))))

(defn -main []
  (println (take 10 (.list (io/file "resources/data")))))
