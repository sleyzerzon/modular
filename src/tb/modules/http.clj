(ns tb.modules.http
  "HTTP Interface to available modules so that 
  could be used a service as well"
  (:require 
    [com.stuartsierra.component             :as component]
    [ring.adapter.jetty                     :refer [run-jetty]]
    [ring.util.response                     :refer [content-type]]
    [ring.middleware.params                 :refer [wrap-params]]
    [compojure.core                         :refer [defroutes GET POST DELETE ANY HEAD context]]
    [compojure.handler                      :refer [site]]
    [compojure.route                        :as route]
    [cheshire.core                          :refer :all]
    [taoensso.timbre                        :as timbre
         :refer (log trace debug info warn error fatal report sometimes)]))

(defn wrap-json-response
  "Middleware that converts responses with a map for a body into a JSON
  response."
  [handler]
  (fn [request]
    (let [response (handler request)]
      (if (map? (:body response))
        (-> response
            (content-type "application/json")
            (update-in [:body] generate-string))
        response))))

(defn resolve-query
  [request servant]
  (let [query (-> request :params :q)]
    (servant query)))

(defrecord HTTP [port user-agent ip-lookup server]
  component/Lifecycle

  (start [this]
    (info "Starting HTTP Component")
    (let []
      (try 
        (defroutes app-routes
          (HEAD "/" [] "")
          (GET "/"  request {:status  200 :body "ok" })
          (GET "/ping" request {:status  200 :body "pong" })
          (GET "/ua" request {:status  200 :body (resolve-query request (:func user-agent)) })
          (GET "/ip" request {:status  200 :body (resolve-query request (:func ip-lookup)) })

          (route/not-found "<p>Page not found.</p>"))
        
        (def http-handler
          (site #'app-routes))

        (def app
          (-> http-handler
            (wrap-params)
            (wrap-json-response)
            ))

        (let [server (run-jetty app {:port port :join? false})]
          (info "Awaiting HTTP requests now on port " port)
          (assoc this :server server))

        (catch Throwable t 
          (do 
            (error t))))))

  (stop [this]
    (.stop server)))

(defn http-server
  [port]
  (map->HTTP {:port port}))
