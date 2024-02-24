package ch.digitalfondue.mjml4j;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UtilsTest {

    @Test
    void testParseUnit() {
        var em = CssUnitParser.parse("32em");
        Assertions.assertEquals("em", em.unit);
        Assertions.assertEquals(32., em.value, 0.001);


        var defaultPx = CssUnitParser.parse("10");
        Assertions.assertEquals("px", defaultPx.unit);
        Assertions.assertTrue(defaultPx.isPx());
        Assertions.assertEquals(10., defaultPx.value, 0.001);
    }

    @Test
    void testMergeOutlookConditionals() {
        Assertions.assertEquals("", Utils.mergeOutlookConditionals("<![endif]--><!--[if mso | IE]>"));

        Assertions.assertEquals(
                "\n    </tr>\n    \n    </td>",
                Utils.mergeOutlookConditionals("\n    </tr>\n    <![endif]-->\n    <!--[if mso | IE]>\n    </td>"));

        Assertions.assertEquals("</div>\n              <!--[if mso | IE]>\n            </td>\n          \n        </tr>\n      \n                  </table>\n                <![endif]-->\n            </td>\n          </tr>\n        </tbody>\n      </table>\n    </div>\n    <!--[if mso | IE]>\n          </td>",
                Utils.mergeOutlookConditionals("</div>\n              <!--[if mso | IE]>\n            </td>\n          <![endif]-->\n              <!--[if mso | IE]>\n        </tr>\n      <![endif]-->\n              <!--[if mso | IE]>\n                  </table>\n                <![endif]-->\n            </td>\n          </tr>\n        </tbody>\n      </table>\n    </div>\n    <!--[if mso | IE]>\n          </td>")
        );
    }

    @Test
    void testMinifyOutlookConditionals() {
        Assertions.assertEquals(
                "\n      <div\n         style=\"\"\n      >\n        \n      \n      <!--[if mso | IE]><table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"\" style=\"width:600px;\" width=\"600\" ><tr><td style=\"line-height:0px;font-size:0px;mso-line-height-rule:exactly;\"><![endif]-->\n    \n      \n      <div  style=\"Margin:0px auto;max-width:600px;\">\n        \n        <table\n           align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"width:100%;\"\n        >\n          <tbody>\n            <tr>\n              <td\n                 style=\"direction:ltr;font-size:0px;padding:20px 0;text-align:center;vertical-align:top;\"\n              >\n                <!--[if mso | IE]><table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"><![endif]-->\n                  \n      <!--[if mso | IE]><tr><![endif]-->\n      \n          <!--[if mso | IE]><td class=\"\" style=\"vertical-align:top;width:600px;\" ><![endif]-->\n            \n      <div\n         class=\"mj-column-per-100 mj-outlook-group-fix\" style=\"font-size:13px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:100%;\"\n      >\n        \n      <table\n         border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"vertical-align:top;\" width=\"100%\"\n      >\n        \n            <tr>\n              <td\n                 align=\"left\" style=\"font-size:0px;padding:10px 25px;word-break:break-word;\"\n              >\n                \n      <div\n         style=\"font-family:helvetica;font-size:20px;line-height:1;text-align:left;color:#F45E43;\"\n      >\n        Hello World\n      </div>\n    \n              </td>\n            </tr>\n          \n      </table>\n    \n      </div>\n    \n          <!--[if mso | IE]></td><![endif]-->\n    \n\n      <!--[if mso | IE]></tr><![endif]-->\n    \n                <!--[if mso | IE]></table><![endif]-->\n              </td>\n            </tr>\n          </tbody>\n        </table>\n        \n      </div>\n    \n      \n      <!--[if mso | IE]></td></tr></table><![endif]-->\n    \n    \n      </div>\n",
                Utils.minifyOutlookConditionals("\n      <div\n         style=\"\"\n      >\n        \n      \n      <!--[if mso | IE]>\n      <table\n         align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"\" style=\"width:600px;\" width=\"600\"\n      >\n        <tr>\n          <td style=\"line-height:0px;font-size:0px;mso-line-height-rule:exactly;\">\n      <![endif]-->\n    \n      \n      <div  style=\"Margin:0px auto;max-width:600px;\">\n        \n        <table\n           align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"width:100%;\"\n        >\n          <tbody>\n            <tr>\n              <td\n                 style=\"direction:ltr;font-size:0px;padding:20px 0;text-align:center;vertical-align:top;\"\n              >\n                <!--[if mso | IE]>\n                  <table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n                <![endif]-->\n                  \n      <!--[if mso | IE]>\n        <tr>\n      <![endif]-->\n      \n          <!--[if mso | IE]>\n            <td\n               class=\"\" style=\"vertical-align:top;width:600px;\"\n            >\n          <![endif]-->\n            \n      <div\n         class=\"mj-column-per-100 mj-outlook-group-fix\" style=\"font-size:13px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:100%;\"\n      >\n        \n      <table\n         border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"vertical-align:top;\" width=\"100%\"\n      >\n        \n            <tr>\n              <td\n                 align=\"left\" style=\"font-size:0px;padding:10px 25px;word-break:break-word;\"\n              >\n                \n      <div\n         style=\"font-family:helvetica;font-size:20px;line-height:1;text-align:left;color:#F45E43;\"\n      >\n        Hello World\n      </div>\n    \n              </td>\n            </tr>\n          \n      </table>\n    \n      </div>\n    \n          <!--[if mso | IE]>\n            </td>\n          <![endif]-->\n    \n\n      <!--[if mso | IE]>\n        </tr>\n      <![endif]-->\n    \n                <!--[if mso | IE]>\n                  </table>\n                <![endif]-->\n              </td>\n            </tr>\n          </tbody>\n        </table>\n        \n      </div>\n    \n      \n      <!--[if mso | IE]>\n          </td>\n        </tr>\n      </table>\n      <![endif]-->\n    \n    \n      </div>\n")
        );
    }
}
