package top.frankyang.pre.api.text;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.LowercaseEnumTypeAdapterFactory;
import net.minecraft.util.Util;
import top.frankyang.pre.api.misc.conversion.Castable;
import top.frankyang.pre.api.misc.Copyable;
import top.frankyang.pre.api.misc.conversion.JsonCastable;

import java.util.List;
import java.util.stream.Collectors;

public interface RichText extends Castable<MutableText>, JsonCastable, Copyable<RichText>, CharSequence {
    Gson GSON = Util.make(() -> {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.disableHtmlEscaping();
        gsonBuilder.registerTypeHierarchyAdapter(Text.class, new Text.Serializer());
        gsonBuilder.registerTypeHierarchyAdapter(Style.class, new Style.Serializer());
        gsonBuilder.registerTypeAdapterFactory(new LowercaseEnumTypeAdapterFactory());
        return gsonBuilder.create();
    });

    static RichText ofJson(JsonElement element) {
        return new RichTextImpl(Text.Serializer.fromJson(element));
    }

    @Override
    default int length() {
        return toString().length();
    }

    @Override
    default char charAt(int index) {
        return toString().charAt(index);
    }

    @Override
    default CharSequence subSequence(int start, int end) {
        return toString().subSequence(start, end);
    }

    default List<RichText> getSiblings() {
        return cast().getSiblings().stream().map(RichTextImpl::new).collect(Collectors.toList());
    }

    default TextStyle getStyle() {
        return TextStyle.of(cast().getStyle());
    }

    default void setStyle(TextStyle value) {
        cast().setStyle(value.cast());
    }

    default void addStyle(TextStyle value) {
        cast().fillStyle(value.cast());
    }

    default void addFragment(RichText text) {
        cast().append(text.cast());
    }

    default void addFragment(String string) {
        cast().append(string);
    }

    @Override
    default JsonElement toJson() {
        return GSON.toJsonTree(cast());
    }
}
