package fr.exratio.jme.devkit.core.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.jme3.math.Quaternion;
import java.io.IOException;

public class QuaternionSerializer extends StdSerializer<Quaternion> {

  public QuaternionSerializer(Class<Quaternion> t) {
    super(t);
  }

  @Override
  public void serialize(Quaternion value, JsonGenerator gen, SerializerProvider provider)
      throws IOException {
    gen.writeStartObject();
    gen.writeNumberField("x", value.getX());
    gen.writeNumberField("y", value.getY());
    gen.writeNumberField("z", value.getZ());
    gen.writeNumberField("w", value.getW());
    gen.writeEndObject();
  }

}
