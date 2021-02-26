package fr.exratio.devkit.core.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.jme3.math.ColorRGBA;
import java.io.IOException;

/**
 * A jackson-json ColorRGBA serializer.
 */
public class ColorRGBASerializer extends StdSerializer<ColorRGBA> {

  public ColorRGBASerializer(Class<ColorRGBA> t) {
    super(t);
  }

  @Override
  public void serialize(ColorRGBA value, JsonGenerator gen, SerializerProvider provider)
      throws IOException {
    gen.writeStartObject();
    gen.writeNumberField("r", value.getRed());
    gen.writeNumberField("g", value.getGreen());
    gen.writeNumberField("b", value.getBlue());
    gen.writeNumberField("a", value.getAlpha());
    gen.writeEndObject();
  }

}
