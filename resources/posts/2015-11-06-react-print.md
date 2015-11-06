:title Printing with React
:published 2015-11-06
:dek Checking the media type with JavaScript
:body

A hot topic in the React community lately is the use of [inline CSS](https://css-tricks.com/the-debate-around-do-we-even-need-css-anymore).
A consequence of this style is that you have to do some heavy lifting yourself. I've written previously about how
this applies to [CSS hovering](/2015-07-14-react-hover), and now I'll tell you how to handle media queries.

I came across this when styling a React component for print. I found a library,
[Radium](https://github.com/FormidableLabs/radium), that has support for inline media queries but it felt
very intrusive.

The solution is pretty simple, there is no need for a large library. Just use the
[matchMedia](http://caniuse.com/#feat=matchmedia) API, it is supported by the majority of browsers.

For fun I created a React mixin, but you could also just put this in your component's componentWillMount function
if you don't need a mixin.

```
const PrintMixin = {
  // Invoked once before the initial rendering occurs
  componentWillMount: function() {
    this.state = this.state || {};
    this.state.isPrinting = false;

    // Run a media query through the matchMedia API
    const query = window.matchMedia('print')
    const queryListener = function(m) {
      this.setState({isPrinting: m.matches});
    }.bind(this)

    query.addListener(queryListener);
  }
};
```

To render different content depending on if you are printing or not, then
simply apply the PrintMixin and it will add this.state.isPrinting to your
component.

```
const MyComponent = React.createClass({
  // Adds this.state.isPrinting
  mixins: [PrintMixin],

  render: function() {
    return  <div>{(this.state.isPrinting) ? "Printing" : "Screening"}</div>;
  }
});

ReactDOM.render(<Panel/>, document.getElementById("main"))
```

You can find the example code on [github](https://github.com/Odinodin/react-playground), hopefully this
 will help someone who tries to print with inline CSS in React.