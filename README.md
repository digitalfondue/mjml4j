# MJML4J

A java based [mjml](https://mjml.io/) implementation.

Require at least java 17.

Javadoc: https://javadoc.io/doc/ch.digitalfondue.mjml4j/mjml4j

# Why

As far as I know, there is no pure java porting of mjml. This library is quite compact (~150Kb) with a single dependency - the [html5 parser (jfiveparse)](https://github.com/digitalfondue/jfiveparse) (~149kb).

# License

mjml4j is licensed under the MIT License.

The code is based on the following projects:

 - https://github.com/mjmlio/mjml/
 - https://github.com/SebastianStehle/mjml-net
 - https://github.com/LiamRiddell/MJML.NET

# Status

Most of the mj-* tags are supported. It's currently missing:

 - ~~mj-include: will be implemented~~ implemented in 1.1.1
 - mj-style: the inline attribute will be ignored: will be supported when the work on css selector support is done. WIP
 - mj-html-attributes: will be supported when the work on css selector support is done. WIP

Additionally, no pretty print/minimization of the output is provided.

# Download

maven:

```xml
<dependency>
   <groupId>ch.digitalfondue.mjml4j</groupId>
   <artifactId>mjml4j</artifactId>
   <version>1.1.3</version>
</dependency>
```

gradle:

```
implementation 'ch.digitalfondue.mjml4j:mjml4j:1.1.3'
```

# Use

If you use it as a module, remember to add `requires ch.digitalfondue.mjml4j;` in your module-info.

The api is quite simple:

```java
package ch.digitalfondue.test;

import ch.digitalfondue.mjml4j.Mjml4j;

public class App {
   public static void main(String[] args) {
      Mjml4j.Configuration configuration = new Mjml4j.Configuration("en");
      String renderedTemplate = Mjml4j.render("""
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
2) a configuration object with language, optionally a direction and an [IncludeResolver](https://javadoc.io/doc/ch.digitalfondue.mjml4j/mjml4j/latest/ch.digitalfondue.mjml4j/ch/digitalfondue/mjml4j/Mjml4j.IncludeResolver.html)


## mj-include support

By default, mjml4j don't have an [IncludeResolver](https://javadoc.io/doc/ch.digitalfondue.mjml4j/mjml4j/latest/ch.digitalfondue.mjml4j/ch/digitalfondue/mjml4j/Mjml4j.IncludeResolver.html) configured, thus `mj-include` will not work out of the box, you must implement or specify yourself.
mjml4j offer 2 implementations:
 - [FileSystemResolver](https://javadoc.io/doc/ch.digitalfondue.mjml4j/mjml4j/latest/ch.digitalfondue.mjml4j/ch/digitalfondue/mjml4j/Mjml4j.FileSystemResolver.html) if your resources are present on the filesystem
 - [SimpleResourceResolver](https://javadoc.io/doc/ch.digitalfondue.mjml4j/mjml4j/latest/ch.digitalfondue.mjml4j/ch/digitalfondue/mjml4j/Mjml4j.SimpleResourceResolver.html) a resolver that need a [ResourceLoader](https://javadoc.io/doc/ch.digitalfondue.mjml4j/mjml4j/latest/ch.digitalfondue.mjml4j/ch/digitalfondue/mjml4j/Mjml4j.ResourceLoader.html) to be implemented


# TODO:
 - ~~mj-include~~ implemented in 1.1.1
 - check https://github.com/mjmlio/mjml/compare/v4.14.1...v4.15.3 , printing especially
 - validation api:
   - add "parent element" check
   - attribute unit type check
 - improve the renderer
 - cleanup/rewrite the box model, kinda hacky
 - more robust handling of invalid input (check mjml behaviour)
 
 
