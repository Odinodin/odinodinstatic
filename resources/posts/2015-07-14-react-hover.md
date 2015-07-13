:title Hovering React styles
:published 2015-07-14
:dek How to hover stuff using inline styles
:body

[React](http://http://facebook.github.io/react/) introduces a number of interesting ideas, one of which is that 
you can [inline your styles](https://vimeo.com/116209150) instead of using external CSS.  However, there are certain 
properties that [can't be inlined](http://stackoverflow.com/questions/1033156/how-to-write-ahover-in-inline-css) 
with CSS alone.

# JavaScript to the rescue
The solution is straightforward; change the style based on mouse events. Let's create a basic label component 
that changes color when the mouse hovers over it.

```
var Label = React.createClass({

    getInitialState : function() {
      return {hovered: false}
    },

    style: function() {
      if (this.state.hovered) {
        return {backgroundColor: "red"}
      } else {
        return {backgroundColor: "grey"}
      }
    },

    onMouseOver : function () {
      this.setState({hovered:true});
    },

    onMouseOut : function () {
      this.setState({hovered:false});
    },

    render: function() {
      return <span onMouseOver={this.onMouseOver} 
                   onMouseOut={this.onMouseOut} 
                   style={this.style()}>Hover me</span>
    }
});
```

# Cleaning up with indirection
Having to add mouse event handlers in every component can quickly become tedious. Let's introduce a level of 
indirection, our very own hover mixin.

The idea is to have the mixin encapsulate the hover functionality. This includes the hover state and the 
mouse listeners.

```
var HoverMixin = {

    // Initial state
    componentWillMount: function() {
      this.state = {hovered: false};
    },
    
    // Attach mouse listeners to the root node of the component
    componentDidMount: function() {
      this.getDOMNode().addEventListener("mouseover", this.onMouseOver);
      this.getDOMNode().addEventListener("mouseout", this.onMouseOut);
    },
    
    // Clean up listeners when the component unmounts
    componentWillUnmount: function() {
      this.getDOMNode().removeEventListener("mouseover", this.onMouseOver);
      this.getDOMNode().removeEventListener("mouseout", this.onMouseOut);
    },
    
    // Mutate state
    onMouseOver: function() {
      this.setState({ hovered: true });
    },
    
    onMouseOut: function() {
      this.setState({ hovered: false });
    }
  };
```

Let's clean up the Label component by hooking up the HoverMixin: 

```
var Label = React.createClass({
    
    // Apply the mixin 
    mixins: [HoverMixin],

    getInitialState : function() {
      return {hovered: false}
    },

    style: function() {
      // this.state.hovered comes from the mixin
      if (this.state.hovered) {
        return {backgroundColor: "red"}
      } else {
        return {backgroundColor: "grey"}
      }
    },

    render: function() {
      return <span onMouseOver={this.onMouseOver} 
                   onMouseOut={this.onMouseOut} 
                   style={this.style()}>Hover me</span>
    }
});
```

# Mixins are great?
In general, I prefer code that spells out and does what it says. Using mixins can be adverserial to readability; 
[all abstractions are leaky](http://www.joelonsoftware.com/articles/LeakyAbstractions.html).
