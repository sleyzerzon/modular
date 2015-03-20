(ns tb.modules.jdbc
  (:require 
    [clojure.java.jdbc :as jdbc])
  )


(defn exec-query-batch
  "Executes a statement in a prepared fashion, and fetches the records in batches of 1000.
  http://stackoverflow.com/questions/19728538/clojure-java-jdbc-query-large-resultset-lazily
  "
  [dbconn sql-and-params result-set-fn & {:keys [fetch-size] :or {fetch-size 1000}}]
  ; (logging/warn sql-and-params)
  (let [sql-query     (first sql-and-params)
        params    (rest sql-and-params)
        stmt       (jdbc/prepare-statement dbconn sql-query :fetch-size fetch-size)]
    
    (jdbc/query dbconn (concat [stmt] params ) :result-set-fn result-set-fn)))