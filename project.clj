(defproject stocksim "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/clojurescript "1.10.520"]
                 [org.clojure/tools.nrepl "0.2.13"]
                 [reagent "0.7.0"]
                 [re-frame "0.10.5"]
                 [compojure "1.5.0"]
                 [yogthos/config "0.8"]
                 [day8.re-frame/http-fx "0.1.5"]
                 [cljs-ajax "0.7.3"]
                 [ring "1.4.0"]
                 [com.rpl/specter "1.1.0"]]

  :plugins [[lein-cljsbuild "1.1.5"]]

  :min-lein-version "2.5.3"

  :source-paths ["src/clj" "src/cljc"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]

  :figwheel {:css-dirs ["resources/public/css"]
             :ring-handler stocksim.handler/dev-handler}

  :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}

  :profiles
  {:dev
   {:dependencies [[binaryage/devtools "0.9.4"]
                   [figwheel-sidecar "0.5.13"]
                   [day8.re-frame/trace "0.1.21"]
                   [com.cemerick/piggieback "0.2.2"]
                   [binaryage/dirac "1.2.35"]]

    :plugins      [[lein-figwheel "0.5.16"]]}
   :repl
   {:repl-options {:port             8230
                   :nrepl-middleware [dirac.nrepl/middleware]
                   :init             (do
                                       (require 'dirac.agent)
                                       (dirac.agent/boot!))}}}

  :cljsbuild
  {:builds
   [{:id           "dev"
     :source-paths ["src/cljs" "src/cljc"]
     :figwheel     {:on-jsload "stocksim.core/mount-root"}
     :compiler     {:main                 stocksim.core
                    :output-to            "resources/public/js/compiled/app.js"
                    :output-dir           "resources/public/js/compiled/out"
                    :asset-path           "js/compiled/out"
                    :source-map-timestamp true
                    :closure-defines      {"re_frame.trace.trace_enabled_QMARK_" true}
                    :preloads             [devtools.preload day8.re-frame.trace.preload dirac.runtime.preload]
                    :external-config      {:devtools/config {:features-to-install :all}}
                    }}

    {:id           "min"
     :source-paths ["src/cljs" "src/cljc"]
     :jar true
     :compiler     {:main            stocksim.core
                    :output-to       "resources/public/js/compiled/app.js"
                    :optimizations   :advanced
                    :closure-defines {goog.DEBUG false}
                    :pretty-print    false}}


    ]}

  :main stocksim.server

  :aot [stocksim.server]

  :uberjar-name "stocksim.jar"

  :prep-tasks [["cljsbuild" "once" "min"] "compile"]
  )
