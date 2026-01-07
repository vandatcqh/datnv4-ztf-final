package org.example.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.59.0)",
    comments = "Source: token_service.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class AccessTokenServiceGrpc {

  private AccessTokenServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "token.service.AccessTokenService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<org.example.grpc.GenerateAccessTokenRequest,
      org.example.grpc.GenerateAccessTokenResponse> getGenerateAccessTokenMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GenerateAccessToken",
      requestType = org.example.grpc.GenerateAccessTokenRequest.class,
      responseType = org.example.grpc.GenerateAccessTokenResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.example.grpc.GenerateAccessTokenRequest,
      org.example.grpc.GenerateAccessTokenResponse> getGenerateAccessTokenMethod() {
    io.grpc.MethodDescriptor<org.example.grpc.GenerateAccessTokenRequest, org.example.grpc.GenerateAccessTokenResponse> getGenerateAccessTokenMethod;
    if ((getGenerateAccessTokenMethod = AccessTokenServiceGrpc.getGenerateAccessTokenMethod) == null) {
      synchronized (AccessTokenServiceGrpc.class) {
        if ((getGenerateAccessTokenMethod = AccessTokenServiceGrpc.getGenerateAccessTokenMethod) == null) {
          AccessTokenServiceGrpc.getGenerateAccessTokenMethod = getGenerateAccessTokenMethod =
              io.grpc.MethodDescriptor.<org.example.grpc.GenerateAccessTokenRequest, org.example.grpc.GenerateAccessTokenResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GenerateAccessToken"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.example.grpc.GenerateAccessTokenRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.example.grpc.GenerateAccessTokenResponse.getDefaultInstance()))
              .setSchemaDescriptor(new AccessTokenServiceMethodDescriptorSupplier("GenerateAccessToken"))
              .build();
        }
      }
    }
    return getGenerateAccessTokenMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.example.grpc.ValidateAccessTokenRequest,
      org.example.grpc.ValidateAccessTokenResponse> getValidateAccessTokenMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ValidateAccessToken",
      requestType = org.example.grpc.ValidateAccessTokenRequest.class,
      responseType = org.example.grpc.ValidateAccessTokenResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.example.grpc.ValidateAccessTokenRequest,
      org.example.grpc.ValidateAccessTokenResponse> getValidateAccessTokenMethod() {
    io.grpc.MethodDescriptor<org.example.grpc.ValidateAccessTokenRequest, org.example.grpc.ValidateAccessTokenResponse> getValidateAccessTokenMethod;
    if ((getValidateAccessTokenMethod = AccessTokenServiceGrpc.getValidateAccessTokenMethod) == null) {
      synchronized (AccessTokenServiceGrpc.class) {
        if ((getValidateAccessTokenMethod = AccessTokenServiceGrpc.getValidateAccessTokenMethod) == null) {
          AccessTokenServiceGrpc.getValidateAccessTokenMethod = getValidateAccessTokenMethod =
              io.grpc.MethodDescriptor.<org.example.grpc.ValidateAccessTokenRequest, org.example.grpc.ValidateAccessTokenResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ValidateAccessToken"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.example.grpc.ValidateAccessTokenRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.example.grpc.ValidateAccessTokenResponse.getDefaultInstance()))
              .setSchemaDescriptor(new AccessTokenServiceMethodDescriptorSupplier("ValidateAccessToken"))
              .build();
        }
      }
    }
    return getValidateAccessTokenMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static AccessTokenServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AccessTokenServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AccessTokenServiceStub>() {
        @java.lang.Override
        public AccessTokenServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AccessTokenServiceStub(channel, callOptions);
        }
      };
    return AccessTokenServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static AccessTokenServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AccessTokenServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AccessTokenServiceBlockingStub>() {
        @java.lang.Override
        public AccessTokenServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AccessTokenServiceBlockingStub(channel, callOptions);
        }
      };
    return AccessTokenServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static AccessTokenServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AccessTokenServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AccessTokenServiceFutureStub>() {
        @java.lang.Override
        public AccessTokenServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AccessTokenServiceFutureStub(channel, callOptions);
        }
      };
    return AccessTokenServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void generateAccessToken(org.example.grpc.GenerateAccessTokenRequest request,
        io.grpc.stub.StreamObserver<org.example.grpc.GenerateAccessTokenResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGenerateAccessTokenMethod(), responseObserver);
    }

    /**
     */
    default void validateAccessToken(org.example.grpc.ValidateAccessTokenRequest request,
        io.grpc.stub.StreamObserver<org.example.grpc.ValidateAccessTokenResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getValidateAccessTokenMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service AccessTokenService.
   */
  public static abstract class AccessTokenServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return AccessTokenServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service AccessTokenService.
   */
  public static final class AccessTokenServiceStub
      extends io.grpc.stub.AbstractAsyncStub<AccessTokenServiceStub> {
    private AccessTokenServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AccessTokenServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AccessTokenServiceStub(channel, callOptions);
    }

    /**
     */
    public void generateAccessToken(org.example.grpc.GenerateAccessTokenRequest request,
        io.grpc.stub.StreamObserver<org.example.grpc.GenerateAccessTokenResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGenerateAccessTokenMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void validateAccessToken(org.example.grpc.ValidateAccessTokenRequest request,
        io.grpc.stub.StreamObserver<org.example.grpc.ValidateAccessTokenResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getValidateAccessTokenMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service AccessTokenService.
   */
  public static final class AccessTokenServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<AccessTokenServiceBlockingStub> {
    private AccessTokenServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AccessTokenServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AccessTokenServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public org.example.grpc.GenerateAccessTokenResponse generateAccessToken(org.example.grpc.GenerateAccessTokenRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGenerateAccessTokenMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.example.grpc.ValidateAccessTokenResponse validateAccessToken(org.example.grpc.ValidateAccessTokenRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getValidateAccessTokenMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service AccessTokenService.
   */
  public static final class AccessTokenServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<AccessTokenServiceFutureStub> {
    private AccessTokenServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AccessTokenServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AccessTokenServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.example.grpc.GenerateAccessTokenResponse> generateAccessToken(
        org.example.grpc.GenerateAccessTokenRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGenerateAccessTokenMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.example.grpc.ValidateAccessTokenResponse> validateAccessToken(
        org.example.grpc.ValidateAccessTokenRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getValidateAccessTokenMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GENERATE_ACCESS_TOKEN = 0;
  private static final int METHODID_VALIDATE_ACCESS_TOKEN = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GENERATE_ACCESS_TOKEN:
          serviceImpl.generateAccessToken((org.example.grpc.GenerateAccessTokenRequest) request,
              (io.grpc.stub.StreamObserver<org.example.grpc.GenerateAccessTokenResponse>) responseObserver);
          break;
        case METHODID_VALIDATE_ACCESS_TOKEN:
          serviceImpl.validateAccessToken((org.example.grpc.ValidateAccessTokenRequest) request,
              (io.grpc.stub.StreamObserver<org.example.grpc.ValidateAccessTokenResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getGenerateAccessTokenMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              org.example.grpc.GenerateAccessTokenRequest,
              org.example.grpc.GenerateAccessTokenResponse>(
                service, METHODID_GENERATE_ACCESS_TOKEN)))
        .addMethod(
          getValidateAccessTokenMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              org.example.grpc.ValidateAccessTokenRequest,
              org.example.grpc.ValidateAccessTokenResponse>(
                service, METHODID_VALIDATE_ACCESS_TOKEN)))
        .build();
  }

  private static abstract class AccessTokenServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    AccessTokenServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return org.example.grpc.TokenServiceProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("AccessTokenService");
    }
  }

  private static final class AccessTokenServiceFileDescriptorSupplier
      extends AccessTokenServiceBaseDescriptorSupplier {
    AccessTokenServiceFileDescriptorSupplier() {}
  }

  private static final class AccessTokenServiceMethodDescriptorSupplier
      extends AccessTokenServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    AccessTokenServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (AccessTokenServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new AccessTokenServiceFileDescriptorSupplier())
              .addMethod(getGenerateAccessTokenMethod())
              .addMethod(getValidateAccessTokenMethod())
              .build();
        }
      }
    }
    return result;
  }
}
