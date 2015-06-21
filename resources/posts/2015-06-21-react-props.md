:title React PropTypes validation 
:published 2015-06-21
:dek Create robust React components 
:body

After using [React](http://http://facebook.github.io/react/) at work for a while, I've experienced 
that as the application grows, it gets increasingly harder to track the shape of the data 
that a particular React component relies on. I found myself having to work backwards up the 
component hierarchy, often all the way to a backend API call in order to figure out how 
the data actually looked like. Obviously a cumbersome and error-prone process.


# PropTypes to the rescue
This is where [PropTypes](https://facebook.github.io/react/docs/reusable-components.html) 
are really helpful. The example below shows their basic usage:

```
var MyComponent = React.createClass({
  propTypes: {
    someOptionalNumber: React.PropTypes.number,
    
    someRequiredString: React.PropTypes.string.isRequired,
    
    anArrayOfStrings: React.PropTypes.arrayOf(React.PropTypes.string),
    
    anObject: React.PropTypes.object
    
  },
  /* ... etc */
})
```

If you fail to provide the required property *someRequiredString*:

```
React.render(<MyComponent />, document.getElementById('content'))
```

then you will get an warning in your console:

```
Warning: 
Failed propType: Required prop `someRequiredString` 
was not specified in `MyComponent`
```


# Shaping up
When the component takes an object as input, it would be helpful to know the shape
of the object; which of its properties to we rely on?

```
var R = React.DOM;

var PersonComp = React.createClass({
  propTypes: {
    person: React.PropTypes.shape({
      name: React.PropTypes.string,
      age: React.PropTypes.number
    }).isRequired
  },
  
  render: function() {
    return R.ul({},
                [R.li({}, this.props.person.name),
                 R.li({}, this.props.person.age)])
  }
})
```

You can also nest shapes, say for instance we want to assert that the person has an address with two properties: 

```
propTypes: {
    person: React.PropTypes.shape({
      name: React.PropTypes.string,
      age: React.PropTypes.number,
      address: React.PropTypes.shape({
        street: React.PropTypes.number.isRequired,
        zip: React.PropTypes.number.isRequired
      })
    }).isRequired
  },
```

Note that the warnings that you get with shapes are not as good as they could have been; you will only be warned about
which properties are missing. An improvement would be if the warning contained the actual object and where in the 
nested shape the missing elements belong. 

# Custom validation
We can take this one step further and add our own validator. A custom validator
is a function with a specific argument list. If the validation fails, you return an
Error object. 

```
var PersonComp = React.createClass({
  
  propTypes: {
    shortString: function(props, propName, componentName) {
      if (props[propName].length > 5) {
        return new Error('The shortString was too long!');
      }
    }
  },
 
  /* ... etc */
})
```

Since the validator is just a function, you could extract and reuse them across components.

# TL;DR
To sum it all up, React.propTypes is a nice feature and you should definitely use it when creating
components. They help you catch errors during development earlier and serve as documentation to help
you remember the assumptions of your components.