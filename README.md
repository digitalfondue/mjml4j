# MJML4J

A java based [mjml](https://mjml.io/) implementation.

Require at least java 17.

# Why

As far as I know, there is no pure java porting of mjml. This library is quite compact (~145Kb) with a single dependency - the [html5 parser (jfiveparse)](https://github.com/digitalfondue/jfiveparse) (~150kb).

# License

mjml4j is licensed under the MIT License.

The code is based on the following projects:

 - https://github.com/mjmlio/mjml/
 - https://github.com/SebastianStehle/mjml-net
 - https://github.com/LiamRiddell/MJML.NET

# Status

Most of the mj-* tags are supported. It's currently missing:

 - mj-include: will be implemented
 - mj-style: the inline attribute will be ignored 
 - mj-html-attributes: will not be supported, as it requires a css selector

Additionally, no pretty print/minimization of the output is provided.

# Download

maven:

```xml
<dependency>
   <groupId>ch.digitalfondue.mjml4j</groupId>
   <artifactId>mjml4j</artifactId>
   <version>1.0.1</version>
</dependency>
```

gradle:

```
implementation 'ch.digitalfondue.mjml4j:mjml4j:1.0.1'
```

# Use

If you use it as a module, remember to add `requires ch.digitalfondue.mjml4j;` in your module-info.

The api is quite simple:

```java
package ch.digitalfondue.test;

import ch.digitalfondue.mjml4j.Mjml4j;

public class App {
   public static void main(String[] args) {
      var configuration = new Mjml4j.Configuration("en");
      var renderedTemplate = Mjml4j.render("""
              <mjml>
                <mj-body>
                  <mj-section>
                    <mj-column>
                                      
                      <mj-image width="100px" src="/assets/img/logo-small.png"></mj-image>
                                      
                      <mj-divider border-color="#F45E43"></mj-divider>
                                      
                      <mj-text font-size="20px" color="#F45E43" font-family="helvetica">Hello World</mj-text>
                                      
                    </mj-column>
                  </mj-section>
                </mj-body>
              </mjml>
              """, configuration);
      System.out.println(renderedTemplate);
   }
}
```

The `render` static method accept as a parameters:
1) a string which will be then parsed and processed by the [html5 parser (jfiveparse)](https://github.com/digitalfondue/jfiveparse), or it can accept a `org.w3c.dom.Document` 
2) a configuration object with language and optionally direction


# TODO:
 - mj-include
 - check https://github.com/mjmlio/mjml/compare/v4.14.1...v4.15.3 , printing especially
 - validation api:
   - add "parent element" check
   - attribute unit type check
 - improve the renderer
 - cleanup/rewrite the box model, kinda hacky
 
 
