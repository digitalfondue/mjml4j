package ch.digitalfondue.mjml4j;

class CssBoxModel {
    final float totalWidth;
    final float borderWidth;
    final float paddingWidth;
    final float boxWidth;

    CssBoxModel(float totalWidth, float borderWidth, float paddingWidth, float boxWidth) {
        this.totalWidth = totalWidth;
        this.borderWidth = borderWidth;
        this.paddingWidth = paddingWidth;
        this.boxWidth = boxWidth;
    }
}
