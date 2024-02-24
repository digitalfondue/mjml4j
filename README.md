# MJML4J

A java based [mjml](https://mjml.io/) implementation.

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

# TODO:
 - fix background positioning logic in section, see disabled test
 - jpackage configuration + release to maven central
 - mj-include
 - check https://github.com/mjmlio/mjml/compare/v4.14.1...v4.15.3 , printing especially
 - validation api:
   - add "parent element" check
   - attribute unit type check
 - improve the renderer
 - cleanup/rewrite the box model, kinda hacky
 
 
