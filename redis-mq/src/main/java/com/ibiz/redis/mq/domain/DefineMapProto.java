// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: DefineMap.proto

package com.ibiz.redis.mq.domain;

public final class DefineMapProto {
  private DefineMapProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface DefineMapOrBuilder extends
      // @@protoc_insertion_point(interface_extends:com.ibiz.redis.mq.domain.DefineMap)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>optional string key = 1;</code>
     */
    boolean hasKey();
    /**
     * <code>optional string key = 1;</code>
     */
    java.lang.String getKey();
    /**
     * <code>optional string key = 1;</code>
     */
    com.google.protobuf.ByteString
        getKeyBytes();

    /**
     * <code>optional string value = 2;</code>
     */
    boolean hasValue();
    /**
     * <code>optional string value = 2;</code>
     */
    java.lang.String getValue();
    /**
     * <code>optional string value = 2;</code>
     */
    com.google.protobuf.ByteString
        getValueBytes();
  }
  /**
   * Protobuf type {@code com.ibiz.redis.mq.domain.DefineMap}
   */
  public static final class DefineMap extends
      com.google.protobuf.GeneratedMessage implements
      // @@protoc_insertion_point(message_implements:com.ibiz.redis.mq.domain.DefineMap)
      DefineMapOrBuilder {
    // Use DefineMap.newBuilder() to construct.
    private DefineMap(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private DefineMap(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final DefineMap defaultInstance;
    public static DefineMap getDefaultInstance() {
      return defaultInstance;
    }

    public DefineMap getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private DefineMap(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 10: {
              com.google.protobuf.ByteString bs = input.readBytes();
              bitField0_ |= 0x00000001;
              key_ = bs;
              break;
            }
            case 18: {
              com.google.protobuf.ByteString bs = input.readBytes();
              bitField0_ |= 0x00000002;
              value_ = bs;
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.ibiz.redis.mq.domain.DefineMapProto.internal_static_com_ibiz_redis_mq_domain_DefineMap_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.ibiz.redis.mq.domain.DefineMapProto.internal_static_com_ibiz_redis_mq_domain_DefineMap_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.ibiz.redis.mq.domain.DefineMapProto.DefineMap.class, com.ibiz.redis.mq.domain.DefineMapProto.DefineMap.Builder.class);
    }

    public static com.google.protobuf.Parser<DefineMap> PARSER =
        new com.google.protobuf.AbstractParser<DefineMap>() {
      public DefineMap parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new DefineMap(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<DefineMap> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    public static final int KEY_FIELD_NUMBER = 1;
    private java.lang.Object key_;
    /**
     * <code>optional string key = 1;</code>
     */
    public boolean hasKey() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>optional string key = 1;</code>
     */
    public java.lang.String getKey() {
      java.lang.Object ref = key_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          key_ = s;
        }
        return s;
      }
    }
    /**
     * <code>optional string key = 1;</code>
     */
    public com.google.protobuf.ByteString
        getKeyBytes() {
      java.lang.Object ref = key_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        key_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int VALUE_FIELD_NUMBER = 2;
    private java.lang.Object value_;
    /**
     * <code>optional string value = 2;</code>
     */
    public boolean hasValue() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>optional string value = 2;</code>
     */
    public java.lang.String getValue() {
      java.lang.Object ref = value_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          value_ = s;
        }
        return s;
      }
    }
    /**
     * <code>optional string value = 2;</code>
     */
    public com.google.protobuf.ByteString
        getValueBytes() {
      java.lang.Object ref = value_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        value_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    private void initFields() {
      key_ = "";
      value_ = "";
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeBytes(1, getKeyBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBytes(2, getValueBytes());
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(1, getKeyBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(2, getValueBytes());
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static com.ibiz.redis.mq.domain.DefineMapProto.DefineMap parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.ibiz.redis.mq.domain.DefineMapProto.DefineMap parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.ibiz.redis.mq.domain.DefineMapProto.DefineMap parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.ibiz.redis.mq.domain.DefineMapProto.DefineMap parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.ibiz.redis.mq.domain.DefineMapProto.DefineMap parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.ibiz.redis.mq.domain.DefineMapProto.DefineMap parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static com.ibiz.redis.mq.domain.DefineMapProto.DefineMap parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static com.ibiz.redis.mq.domain.DefineMapProto.DefineMap parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static com.ibiz.redis.mq.domain.DefineMapProto.DefineMap parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.ibiz.redis.mq.domain.DefineMapProto.DefineMap parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.ibiz.redis.mq.domain.DefineMapProto.DefineMap prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code com.ibiz.redis.mq.domain.DefineMap}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:com.ibiz.redis.mq.domain.DefineMap)
        com.ibiz.redis.mq.domain.DefineMapProto.DefineMapOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.ibiz.redis.mq.domain.DefineMapProto.internal_static_com_ibiz_redis_mq_domain_DefineMap_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.ibiz.redis.mq.domain.DefineMapProto.internal_static_com_ibiz_redis_mq_domain_DefineMap_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.ibiz.redis.mq.domain.DefineMapProto.DefineMap.class, com.ibiz.redis.mq.domain.DefineMapProto.DefineMap.Builder.class);
      }

      // Construct using com.ibiz.redis.mq.domain.DefineMapProto.DefineMap.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        key_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        value_ = "";
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.ibiz.redis.mq.domain.DefineMapProto.internal_static_com_ibiz_redis_mq_domain_DefineMap_descriptor;
      }

      public com.ibiz.redis.mq.domain.DefineMapProto.DefineMap getDefaultInstanceForType() {
        return com.ibiz.redis.mq.domain.DefineMapProto.DefineMap.getDefaultInstance();
      }

      public com.ibiz.redis.mq.domain.DefineMapProto.DefineMap build() {
        com.ibiz.redis.mq.domain.DefineMapProto.DefineMap result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public com.ibiz.redis.mq.domain.DefineMapProto.DefineMap buildPartial() {
        com.ibiz.redis.mq.domain.DefineMapProto.DefineMap result = new com.ibiz.redis.mq.domain.DefineMapProto.DefineMap(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.key_ = key_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.value_ = value_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.ibiz.redis.mq.domain.DefineMapProto.DefineMap) {
          return mergeFrom((com.ibiz.redis.mq.domain.DefineMapProto.DefineMap)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.ibiz.redis.mq.domain.DefineMapProto.DefineMap other) {
        if (other == com.ibiz.redis.mq.domain.DefineMapProto.DefineMap.getDefaultInstance()) return this;
        if (other.hasKey()) {
          bitField0_ |= 0x00000001;
          key_ = other.key_;
          onChanged();
        }
        if (other.hasValue()) {
          bitField0_ |= 0x00000002;
          value_ = other.value_;
          onChanged();
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.ibiz.redis.mq.domain.DefineMapProto.DefineMap parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.ibiz.redis.mq.domain.DefineMapProto.DefineMap) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private java.lang.Object key_ = "";
      /**
       * <code>optional string key = 1;</code>
       */
      public boolean hasKey() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>optional string key = 1;</code>
       */
      public java.lang.String getKey() {
        java.lang.Object ref = key_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            key_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>optional string key = 1;</code>
       */
      public com.google.protobuf.ByteString
          getKeyBytes() {
        java.lang.Object ref = key_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          key_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string key = 1;</code>
       */
      public Builder setKey(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        key_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string key = 1;</code>
       */
      public Builder clearKey() {
        bitField0_ = (bitField0_ & ~0x00000001);
        key_ = getDefaultInstance().getKey();
        onChanged();
        return this;
      }
      /**
       * <code>optional string key = 1;</code>
       */
      public Builder setKeyBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        key_ = value;
        onChanged();
        return this;
      }

      private java.lang.Object value_ = "";
      /**
       * <code>optional string value = 2;</code>
       */
      public boolean hasValue() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>optional string value = 2;</code>
       */
      public java.lang.String getValue() {
        java.lang.Object ref = value_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            value_ = s;
          }
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>optional string value = 2;</code>
       */
      public com.google.protobuf.ByteString
          getValueBytes() {
        java.lang.Object ref = value_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          value_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string value = 2;</code>
       */
      public Builder setValue(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        value_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string value = 2;</code>
       */
      public Builder clearValue() {
        bitField0_ = (bitField0_ & ~0x00000002);
        value_ = getDefaultInstance().getValue();
        onChanged();
        return this;
      }
      /**
       * <code>optional string value = 2;</code>
       */
      public Builder setValueBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        value_ = value;
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:com.ibiz.redis.mq.domain.DefineMap)
    }

    static {
      defaultInstance = new DefineMap(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:com.ibiz.redis.mq.domain.DefineMap)
  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_ibiz_redis_mq_domain_DefineMap_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_com_ibiz_redis_mq_domain_DefineMap_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\017DefineMap.proto\022\030com.ibiz.redis.mq.dom" +
      "ain\"\'\n\tDefineMap\022\013\n\003key\030\001 \001(\t\022\r\n\005value\030\002" +
      " \001(\tB*\n\030com.ibiz.redis.mq.domainB\016Define" +
      "MapProto"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    internal_static_com_ibiz_redis_mq_domain_DefineMap_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_com_ibiz_redis_mq_domain_DefineMap_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_com_ibiz_redis_mq_domain_DefineMap_descriptor,
        new java.lang.String[] { "Key", "Value", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
