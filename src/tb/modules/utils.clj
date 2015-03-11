(ns tb.modules.utils)


(defn select-values
  "clojure.core contains select-keys 
  but not select-values."
  [m ks]
  (reduce 
    #(if-let [v (m %2)] 
        (conj %1 v) %1) 
    [] ks))


(def utc-formatter (doto
                    (java.text.SimpleDateFormat. "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    (.setTimeZone (java.util.TimeZone/getTimeZone "UTC"))))


(defn ep->date
  [epoch]
  (java.util.Date. epoch))

(defn date->utc-str
  [date]
  (.format utc-formatter date))

(defn ep->utc-str
  [epoch]
  (date->utc-str (ep->date epoch))
  )