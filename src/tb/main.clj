(ns tb.main
  (:require 
    [clojure.tools.cli                      :refer [parse-opts]]
    [com.stuartsierra.component             :as component]
    [tb.ua                                  :refer [user-agents-component] :as ua]
    [tb.ip                                  :refer [ip-component]]
    [tb.http                                :refer [http-server]]
    [taoensso.timbre                        :as timbre
         :refer (log trace debug info warn error fatal report sometimes)])
  (:gen-class))

(defn app-system 
  [options]
  (-> (component/system-map 
        :user-agent (user-agents-component)
        :ip-lookup (ip-component (:ipdb options))
        :app (component/using 
                (http-server (:port options))
                [:user-agent :ip-lookup]))))

(def cli-options 
  [["-p" "--port PORT" "Port number"
    :default 8888
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 % 0x10000) "Must be a number between 0 and 65536"]]
    [nil "--ipdb LOCATION" "MaxMind DB File location" :default "/opt/geoip2/GeoLite2-City.mmdb"]
    ])

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [{:keys [options summary errors]} (parse-opts args cli-options)]
    (when (:help options)
      (println summary)
      (System/exit 0))

    (let [sys (component/start (app-system options))]
        (info "System started.."))))
