(ns tb.modules.ua
  "User Agent utilities. Converts a User-Agent string into humanly readable 
  parts. Like Browser, Operating System, Device."
  (:use [clojure.core.memoize :only [memo]])
  (:require 
    [com.stuartsierra.component             :as component]
    [taoensso.timbre                        :as timbre
         :refer (log trace debug info warn error fatal report sometimes)])

  (:import [eu.bitwalker.useragentutils UserAgent DeviceType Browser OperatingSystem]))



(defn str->features
  [string]
  (try
    (let [user-agent (UserAgent. (or string ""))]
      {:browser (-> user-agent .getBrowser .getGroup .getName)
       :os (-> user-agent .getOperatingSystem .getGroup .getName)
       :device (-> user-agent .getOperatingSystem .getDeviceType .getName)})
    (catch Exception e
      (error (str "Could not derive the user-agent from " string) e)
      (str->features nil)
      )))


(def possible-features
  (memo 
    (fn []
      {:os (set (map #(-> % .getGroup .getName) (OperatingSystem/values)))
       :browser (set (map #(-> % .getGroup .getName) (Browser/values)))
       :device (set (map #(.getName %) (DeviceType/values)))})))


(defrecord UserAgents [ func ]
  component/Lifecycle

  (start [this]

    (assoc this :func str->features)
    )

  (stop [this]

    ))

(defn user-agents-component
  []
  (map->UserAgents {}))
