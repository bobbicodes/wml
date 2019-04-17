(ns wml.core
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

(def prices
  (with-open [reader (io/reader "resources/data/prices.csv")]
    (doall
      (csv/read-csv reader))))

(def receipt
  (line-seq (io/reader "resources/data/2001-09-01/ee62f687-4ed4-d4bd-40d9-c2671edb2768")))

(defn void? [s]
  (clojure.string/includes? s "VOID"))

(defn product-code [s]
  (re-find #"\d{10}" s))

(defn price [s]
  (if (re-find #"\d+\.\d+" s)
    (re-find #"\d+\.\d+" s)
    "0.00"))

(defn get-price [s]
  (first (remove nil? (map #(if (clojure.string/includes? % s) (last %)) prices))))

(defn sub-price [line]
  (- (Float/parseFloat (price line))
     (Float/parseFloat (get-price (product-code line)))))

(def receipt-map
  (for [i (range 1 (dec (count receipt)))]
  {:line i
   :code (product-code (nth receipt i))
   :price-charged (price (nth receipt i))
   :actual-price (get-price (product-code (nth receipt i)))
   :price-diff (sub-price (nth receipt i))
   :void (void? (nth receipt (inc i)))}))

(def items
  (filter #(and
            (:price-charged %)
            (not= "0.00" (:price-charged %))
            (:actual-price %)
            (false? (:void %)))
          receipt-map))


(defn -main []
  (println (reduce + (map #(:price-diff %) items))))
