(defproject tb.modules "0.1.0"
  :description "Utility modules as a library/service"
  :url "http://engineering.travelbird.com/open-source/modular"
  :license {:name "MIT"
            :url "http://github.com/travelbird/modular/tree/master/LICENCE"}
  :dependencies [
          [org.clojure/clojure "1.6.0"]
          [org.clojure/tools.cli "0.3.1"] 
          [org.clojure/core.memoize "0.5.6"]
          [com.stuartsierra/component "0.2.3"]
          [com.maxmind.geoip2/geoip2 "2.1.0"]
          [compojure "1.3.1"]
          [ring "1.3.2"]
          [com.taoensso/timbre "3.3.1-1cd4b70" :exclusions [org.clojure/tools.reader]]
          [cheshire "5.4.0"]
          [prismatic/schema "0.4.0"]
          [eu.bitwalker/UserAgentUtils "1.15"]
          [org.clojure/java.jdbc "0.3.6"]
          ]
  :main ^:skip-aot tb.modules.main
  :target-path "target/%s"
  :repositories {
    "conjars" "http://conjars.org/repo/"
    "sonatype-oss-public" "https://oss.sonatype.org/content/groups/public/"}
  :profiles {:uberjar {:aot :all}
            :dev {:plugins [[lein-midje "3.1.3"]]
                   :dependencies [[midje "1.6.0" :exclusions [org.clojure/clojure]]]}
  })