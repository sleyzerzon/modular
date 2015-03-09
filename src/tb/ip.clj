(ns tb.ip
  "Uses MaxMind mmdb file to resolve IP addresses location. "
  (:require 
    [com.stuartsierra.component             :as component]
    [taoensso.timbre                        :as timbre
         :refer (log  trace  debug  info  warn  error  fatal  report sometimes)])
  )


(defn get-country 
  [db-reader ip]
  (let [response (.city db-reader (java.net.InetAddress/getByName ip))]

    {:country (.getName (.getCountry response))
      :city (.getName (.getCity response))}
    )
  )

(defrecord IP [dbfile func]
  component/Lifecycle

  (start [this]
    (let [db (java.io.File. (or dbfile "/opt/geoip2/GeoLite2-City.mmdb"))
          db-reader (.build (com.maxmind.geoip2.DatabaseReader$Builder. db))]
    
    (assoc this :func (partial get-country db-reader))
    ))

  (stop [this]
    (dissoc this :func nil)
    ))

(defn ip-component
  [dbfile]
  (map->IP {:dbfile dbfile}))


