(defproject cig "0.0.1"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [org.clojure/clojurescript "0.0-1450"]]
  :source-paths ["src/cljs"]
  :plugins [[lein-cljsbuild "0.3.0"]
            [lein-ring "0.7.0"]]
  :hooks [leiningen.cljsbuild]
  :min-lein-version "2.0.0"
  :cljsbuild
    {:builds
       [{:source-paths ["src/cljs"],
         :id "dev",
         :compiler
         {:pretty-print true,
          :output-to "resources/public/js/main-debug.js",
          :optimizations :simple}}
        {:source-paths ["test/cljs"],
         :id "test",
         :compiler
         {:pretty-print true,
          :output-to "resources/private/js/unit-test.js",
          :optimizations :whitespace}}
        {:source-paths ["src/cljs"],
         :id "prod",
         :compiler
         {:pretty-print false,
          :output-to "resources/public/js/main.js",
          :optimizations :advanced}}],
     :test-commands
       {"unit"
        ["phantomjs"
         "phantom/unit-test.js"
         "resources/private/html/unit-test.html"]},
     :repl-listen-port 9000,
     :repl-launch-commands
       {"phantom"
          ["phantomjs" "phantom/repl.js" :stdout ".repl-phantom-out" :stderr ".repl-phantom-err"],
        "phantom-naked"
          ["phantomjs" "phantom/repl.js" "resources/private/html/naked.html" :stdout ".repl-phantom-naked-out" :stderr ".repl-phantom-naked-err"],
        "firefox"
          ["firefox" :stdout ".repl-firefox-out" :stderr ".repl-firefox-err"]}}
  :ring {:handler example.routes/app})
