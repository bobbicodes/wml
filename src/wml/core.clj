(ns wml.core
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

(def prices
  (with-open [reader (io/reader "resources/data/prices.csv")]
  (doall
    (csv/read-csv reader))))

(defn -main []
  (println (take 10 (.list (io/file "resources/data")))))
