package org.beanlane;

import org.beanlane.formatter.CapitalizationFormatter;
import org.beanlane.formatter.GetterFormatter;

@BeanPropertyExtractor(formatter = {@BeanPropertyFormatter(value = GetterFormatter.class), @BeanPropertyFormatter(value = CapitalizationFormatter.class, args = "false")})
@VisibleForPackage
class BeanLaneNotBeanConventionGetterTest extends PojoTestCase {
}


