(defproject melee "0.1.0-SNAPSHOT"
  :description "Distributed coordinator"
  :url "https://github.com/wayoutmind/melee"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/core.async "0.1.267.0-0d7780-alpha"]
                 [com.google.guava/guava "16.0.1"]
                 [com.github.sbtourist/journalio "1.4.2"]]
  :profiles {:dev { :dependencies [[org.clojure/tools.namespace "0.2.4"]
                                   [org.clojure/tools.nrepl "0.2.3"]
                                   [org.clojure/java.classpath "0.2.2"]
                                   [midje "1.9.1"]
                                   [bultitude "0.2.6"]]
                    :plugins [[lein-midje "3.1.1"]
                              [lein-marginalia "0.7.1"]
                              [lein-kibit "0.0.8"]
                              [lein-bikeshed "0.1.6"]]}}
  ; :global-vars {*warn-on-reflection* true}
  )
