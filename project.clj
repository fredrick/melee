(defproject melee "0.1.0-SNAPSHOT"
  :description "Distributed coordinator"
  :url "https://github.com/scij/melee"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [com.google.guava/guava "16.0.1"]]
  :profiles {:dev { :dependencies [[midje "1.9.1"]]
                    :plugins [[lein-midje "3.1.1"]
                              [lein-kibit "0.1.6"]]}}
  )
