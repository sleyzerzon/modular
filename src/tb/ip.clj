(ns tb.ip
  ; (:use [clojure.core.memoize :only [memo]])
  ; (:require )
  )


(def db (java.io.File. "/opt/geoip2/GeoLite2-City.mmdb"))
(def db-reader (.build (com.maxmind.geoip2.DatabaseReader$Builder. db)))


(defn get-country 
  [ip]
  (let [response (.city db-reader (java.net.InetAddress/getByName ip))]

    {:country (.getName (.getCountry response))
      :city (.getName (.getCity response))}
    )
  )
