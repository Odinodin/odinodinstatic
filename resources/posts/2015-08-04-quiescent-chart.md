:title Quiescent and Highcharts
:published 2015-08-04
:dek How to glue them together
:body

I recently rewrote the front-end of a [side-project](http://github.com/odinodin/cashflow) from JavaScript and React 
to ClojureScript and [Quiescent](https://github.com/levand/quiescent).
In that effort I needed to chart some data. I could not find a ClojureScript charting library that I was happy with, 
so I decided to keep using [HighCharts](http://highcharts.com) for the moment.
 
# Add Highcharts to project
If you want to use a JavaScript library in a ClojureScript project, the easiest way is to go with [CLJSJS](http://cljsjs.github.io/).
However, not every library is packaged up by CLJSJS. So in order to use Highcharts you have to put a little more 
effort into it.  

Here is how you include it in your [Leiningen project.clj](https://github.com/Odinodin/cashflow/blob/master/quiescentgui/project.clj#L46)

```clj
 :compiler {
    ...
  
   ;; Highrise standalone-framework makes Highrise work without jQuery
   :foreign-libs [{:file "lib/standalone-framework.src.js" 
                   :provides ["Standalone"]}
                  {:file "lib/highcharts.src.js" 
                   :provides ["Highcharts"] 
                   :requires ["Standalone"]}]}}]}
```

# Render a graph
To render a graph component with Quiescent, you have to use one of the React lifecycle hooks that [Quiescent provides](https://github.com/levand/quiescent/blob/release/docs.md#component-options). 
In addition, you have to tell Highcharts which DOM-element to use for the graph. I banged my head against the wall 
trying to get the graph to render by passing a DOM element string id to Highcarts. The trick was passing an actual DOM element reference
 instead. 

```clj
(q/defcomponent MyGraph

  ;; Quiescent will call the :on-render function every time the DOM is updated. 
  ;; It is equivalent to 'componentDidMount' and 'componentDidUpdate' in React 
  :on-render (fn [dom-node component-value]
              
              ;; Instantiate a new chart by targeting the dom-node
              (new js/Highcharts.Chart
              
                   ;; Deep-convert the clojure data structure into a JavaScript 
                   ;; object that Highcharts.Chart takes as input                  
                   (clj->js
                      
                     ;; Need to pass the dom-node reference to :renderTo
                     {:chart {:type    "column"                       
                              :renderTo dom-node}
                       
                      ;; Just some bogus data
                      :series [{:name "Monkey",
                                :data [1, 0, 4]}, 
                               {:name "Giraffe",
                                :data [5, 7, 3]}]})))
  [props]
  ;; This div will be the target of the graphs :renderTo  
  (d/div {}))
```               
        
You can find a complete example in my [Cashflow repo](https://github.com/Odinodin/cashflow/blob/master/quiescentgui/cljs/cashflow/graphs_page.cljs#L55).