(ns tb.main
  (:require 
    [clojure.tools.cli                      :refer [parse-opts]]
    [tb.ua :as ua]
    [tb.ip :refer [get-country]]
    )
  (:gen-class))

(def cli-options 
  [["-p" "--port PORT" "Port number"
    :default 8888
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 % 0x10000) "Must be a number between 0 and 65536"]]])

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [{:keys [options summary errors]} (parse-opts args cli-options)]
    (when (:help options)
      (println summary)
      (System/exit 0)))

  (println (get-country (first args)))
  )
