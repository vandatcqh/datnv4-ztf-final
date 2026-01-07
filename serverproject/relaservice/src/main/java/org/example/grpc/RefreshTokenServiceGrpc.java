package org.example.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.59.0)",
    comments = "Source: token_service.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class RefreshTokenServiceGrpc {

  private RefreshTokenServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "token.service.RefreshTokenService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<org.example.grpc.CreateRefreshTokenRequest,
      org.example.grpc.CreateRefreshTokenResponse> getCreateRefreshTokenMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreateRefreshToken",
      requestType = org.example.grpc.CreateRefreshTokenRequest.class,
      responseType = org.example.grpc.CreateRefreshTokenResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.example.grpc.CreateRefreshTokenRequest,
      org.example.grpc.CreateRefreshTokenResponse> getCreateRefreshTokenMethod() {
    io.grpc.MethodDescriptor<org.example.grpc.CreateRefreshTokenRequest, org.example.grpc.CreateRefreshTokenResponse> getCreateRefreshTokenMethod;
    if ((getCreateRefreshTokenMethod = RefreshTokenServiceGrpc.getCreateRefreshTokenMethod) == null) {
      synchronized (RefreshTokenServiceGrpc.class) {
        if ((getCreateRefreshTokenMethod = RefreshTokenServiceGrpc.getCreateRefreshTokenMethod) == null) {
          RefreshTokenServiceGrpc.getCreateRefreshTokenMethod = getCreateRefreshTokenMethod =
              io.grpc.MethodDescriptor.<org.example.grpc.CreateRefreshTokenRequest, org.example.grpc.CreateRefreshTokenResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CreateRefreshToken"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.example.grpc.CreateRefreshTokenRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.example.grpc.CreateRefreshTokenResponse.getDefaultInstance()))
              .setSchemaDescriptor(new RefreshTokenServiceMethodDescriptorSupplier("CreateRefreshToken"))
              .build();
        }
      }
    }
    return getCreateRefreshTokenMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.example.grpc.RotateRefreshTokenRequest,
      org.example.grpc.RotateRefreshTokenResponse> getRotateRefreshTokenMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "RotateRefreshToken",
      requestType = org.example.grpc.RotateRefreshTokenRequest.class,
      responseType = org.example.grpc.RotateRefreshTokenResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.example.grpc.RotateRefreshTokenRequest,
      org.example.grpc.RotateRefreshTokenResponse> getRotateRefreshTokenMethod() {
    io.grpc.MethodDescriptor<org.example.grpc.RotateRefreshTokenRequest, org.example.grpc.RotateRefreshTokenResponse> getRotateRefreshTokenMethod;
    if ((getRotateRefreshTokenMethod = RefreshTokenServiceGrpc.getRotateRefreshTokenMethod) == null) {
      synchronized (RefreshTokenServiceGrpc.class) {
        if ((getRotateRefreshTokenMethod = RefreshTokenServiceGrpc.getRotateRefreshTokenMethod) == null) {
          RefreshTokenServiceGrpc.getRotateRefreshTokenMethod = getRotateRefreshTokenMethod =
              io.grpc.MethodDescriptor.<org.example.grpc.RotateRefreshTokenRequest, org.example.grpc.RotateRefreshTokenResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "RotateRefreshToken"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.example.grpc.RotateRefreshTokenRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.example.grpc.RotateRefreshTokenResponse.getDefaultInstance()))
              .setSchemaDescriptor(new RefreshTokenServiceMethodDescriptorSupplier("RotateRefreshToken"))
              .build();
        }
      }
    }
    return getRotateRefreshTokenMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.example.grpc.RevokeTokenRequest,
      org.example.grpc.RevokeTokenResponse> getRevokeTokenMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "RevokeToken",
      requestType = org.example.grpc.RevokeTokenRequest.class,
      responseType = org.example.grpc.RevokeTokenResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.example.grpc.RevokeTokenRequest,
      org.example.grpc.RevokeTokenResponse> getRevokeTokenMethod() {
    io.grpc.MethodDescriptor<org.example.grpc.RevokeTokenRequest, org.example.grpc.RevokeTokenResponse> getRevokeTokenMethod;
    if ((getRevokeTokenMethod = RefreshTokenServiceGrpc.getRevokeTokenMethod) == null) {
      synchronized (RefreshTokenServiceGrpc.class) {
        if ((getRevokeTokenMethod = RefreshTokenServiceGrpc.getRevokeTokenMethod) == null) {
          RefreshTokenServiceGrpc.getRevokeTokenMethod = getRevokeTokenMethod =
              io.grpc.MethodDescriptor.<org.example.grpc.RevokeTokenRequest, org.example.grpc.RevokeTokenResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "RevokeToken"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.example.grpc.RevokeTokenRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.example.grpc.RevokeTokenResponse.getDefaultInstance()))
              .setSchemaDescriptor(new RefreshTokenServiceMethodDescriptorSupplier("RevokeToken"))
              .build();
        }
      }
    }
    return getRevokeTokenMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.example.grpc.RevokeAllTokensOfUserRequest,
      org.example.grpc.RevokeAllTokensOfUserResponse> getRevokeAllTokensOfUserMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "RevokeAllTokensOfUser",
      requestType = org.example.grpc.RevokeAllTokensOfUserRequest.class,
      responseType = org.example.grpc.RevokeAllTokensOfUserResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.example.grpc.RevokeAllTokensOfUserRequest,
      org.example.grpc.RevokeAllTokensOfUserResponse> getRevokeAllTokensOfUserMethod() {
    io.grpc.MethodDescriptor<org.example.grpc.RevokeAllTokensOfUserRequest, org.example.grpc.RevokeAllTokensOfUserResponse> getRevokeAllTokensOfUserMethod;
    if ((getRevokeAllTokensOfUserMethod = RefreshTokenServiceGrpc.getRevokeAllTokensOfUserMethod) == null) {
      synchronized (RefreshTokenServiceGrpc.class) {
        if ((getRevokeAllTokensOfUserMethod = RefreshTokenServiceGrpc.getRevokeAllTokensOfUserMethod) == null) {
          RefreshTokenServiceGrpc.getRevokeAllTokensOfUserMethod = getRevokeAllTokensOfUserMethod =
              io.grpc.MethodDescriptor.<org.example.grpc.RevokeAllTokensOfUserRequest, org.example.grpc.RevokeAllTokensOfUserResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "RevokeAllTokensOfUser"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.example.grpc.RevokeAllTokensOfUserRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.example.grpc.RevokeAllTokensOfUserResponse.getDefaultInstance()))
              .setSchemaDescriptor(new RefreshTokenServiceMethodDescriptorSupplier("RevokeAllTokensOfUser"))
              .build();
        }
      }
    }
    return getRevokeAllTokensOfUserMethod;
  }

  private static volatile io.grpc.MethodDescriptor<org.example.grpc.IsValidRequest,
      org.example.grpc.IsValidResponse> getIsValidMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "IsValid",
      requestType = org.example.grpc.IsValidRequest.class,
      responseType = org.example.grpc.IsValidResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.example.grpc.IsValidRequest,
      org.example.grpc.IsValidResponse> getIsValidMethod() {
    io.grpc.MethodDescriptor<org.example.grpc.IsValidRequest, org.example.grpc.IsValidResponse> getIsValidMethod;
    if ((getIsValidMethod = RefreshTokenServiceGrpc.getIsValidMethod) == null) {
      synchronized (RefreshTokenServiceGrpc.class) {
        if ((getIsValidMethod = RefreshTokenServiceGrpc.getIsValidMethod) == null) {
          RefreshTokenServiceGrpc.getIsValidMethod = getIsValidMethod =
              io.grpc.MethodDescriptor.<org.example.grpc.IsValidRequest, org.example.grpc.IsValidResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "IsValid"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.example.grpc.IsValidRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.example.grpc.IsValidResponse.getDefaultInstance()))
              .setSchemaDescriptor(new RefreshTokenServiceMethodDescriptorSupplier("IsValid"))
              .build();
        }
      }
    }
    return getIsValidMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static RefreshTokenServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RefreshTokenServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RefreshTokenServiceStub>() {
        @java.lang.Override
        public RefreshTokenServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RefreshTokenServiceStub(channel, callOptions);
        }
      };
    return RefreshTokenServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static RefreshTokenServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RefreshTokenServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RefreshTokenServiceBlockingStub>() {
        @java.lang.Override
        public RefreshTokenServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RefreshTokenServiceBlockingStub(channel, callOptions);
        }
      };
    return RefreshTokenServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static RefreshTokenServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RefreshTokenServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RefreshTokenServiceFutureStub>() {
        @java.lang.Override
        public RefreshTokenServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RefreshTokenServiceFutureStub(channel, callOptions);
        }
      };
    return RefreshTokenServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void createRefreshToken(org.example.grpc.CreateRefreshTokenRequest request,
        io.grpc.stub.StreamObserver<org.example.grpc.CreateRefreshTokenResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCreateRefreshTokenMethod(), responseObserver);
    }

    /**
     */
    default void rotateRefreshToken(org.example.grpc.RotateRefreshTokenRequest request,
        io.grpc.stub.StreamObserver<org.example.grpc.RotateRefreshTokenResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRotateRefreshTokenMethod(), responseObserver);
    }

    /**
     */
    default void revokeToken(org.example.grpc.RevokeTokenRequest request,
        io.grpc.stub.StreamObserver<org.example.grpc.RevokeTokenResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRevokeTokenMethod(), responseObserver);
    }

    /**
     */
    default void revokeAllTokensOfUser(org.example.grpc.RevokeAllTokensOfUserRequest request,
        io.grpc.stub.StreamObserver<org.example.grpc.RevokeAllTokensOfUserResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRevokeAllTokensOfUserMethod(), responseObserver);
    }

    /**
     */
    default void isValid(org.example.grpc.IsValidRequest request,
        io.grpc.stub.StreamObserver<org.example.grpc.IsValidResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getIsValidMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service RefreshTokenService.
   */
  public static abstract class RefreshTokenServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return RefreshTokenServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service RefreshTokenService.
   */
  public static final class RefreshTokenServiceStub
      extends io.grpc.stub.AbstractAsyncStub<RefreshTokenServiceStub> {
    private RefreshTokenServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RefreshTokenServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RefreshTokenServiceStub(channel, callOptions);
    }

    /**
     */
    public void createRefreshToken(org.example.grpc.CreateRefreshTokenRequest request,
        io.grpc.stub.StreamObserver<org.example.grpc.CreateRefreshTokenResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCreateRefreshTokenMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void rotateRefreshToken(org.example.grpc.RotateRefreshTokenRequest request,
        io.grpc.stub.StreamObserver<org.example.grpc.RotateRefreshTokenResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRotateRefreshTokenMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void revokeToken(org.example.grpc.RevokeTokenRequest request,
        io.grpc.stub.StreamObserver<org.example.grpc.RevokeTokenResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRevokeTokenMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void revokeAllTokensOfUser(org.example.grpc.RevokeAllTokensOfUserRequest request,
        io.grpc.stub.StreamObserver<org.example.grpc.RevokeAllTokensOfUserResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRevokeAllTokensOfUserMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void isValid(org.example.grpc.IsValidRequest request,
        io.grpc.stub.StreamObserver<org.example.grpc.IsValidResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getIsValidMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service RefreshTokenService.
   */
  public static final class RefreshTokenServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<RefreshTokenServiceBlockingStub> {
    private RefreshTokenServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RefreshTokenServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RefreshTokenServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public org.example.grpc.CreateRefreshTokenResponse createRefreshToken(org.example.grpc.CreateRefreshTokenRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreateRefreshTokenMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.example.grpc.RotateRefreshTokenResponse rotateRefreshToken(org.example.grpc.RotateRefreshTokenRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRotateRefreshTokenMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.example.grpc.RevokeTokenResponse revokeToken(org.example.grpc.RevokeTokenRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRevokeTokenMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.example.grpc.RevokeAllTokensOfUserResponse revokeAllTokensOfUser(org.example.grpc.RevokeAllTokensOfUserRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRevokeAllTokensOfUserMethod(), getCallOptions(), request);
    }

    /**
     */
    public org.example.grpc.IsValidResponse isValid(org.example.grpc.IsValidRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getIsValidMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service RefreshTokenService.
   */
  public static final class RefreshTokenServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<RefreshTokenServiceFutureStub> {
    private RefreshTokenServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RefreshTokenServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RefreshTokenServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.example.grpc.CreateRefreshTokenResponse> createRefreshToken(
        org.example.grpc.CreateRefreshTokenRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCreateRefreshTokenMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.example.grpc.RotateRefreshTokenResponse> rotateRefreshToken(
        org.example.grpc.RotateRefreshTokenRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRotateRefreshTokenMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.example.grpc.RevokeTokenResponse> revokeToken(
        org.example.grpc.RevokeTokenRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRevokeTokenMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.example.grpc.RevokeAllTokensOfUserResponse> revokeAllTokensOfUser(
        org.example.grpc.RevokeAllTokensOfUserRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRevokeAllTokensOfUserMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.example.grpc.IsValidResponse> isValid(
        org.example.grpc.IsValidRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getIsValidMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_CREATE_REFRESH_TOKEN = 0;
  private static final int METHODID_ROTATE_REFRESH_TOKEN = 1;
  private static final int METHODID_REVOKE_TOKEN = 2;
  private static final int METHODID_REVOKE_ALL_TOKENS_OF_USER = 3;
  private static final int METHODID_IS_VALID = 4;

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
        case METHODID_CREATE_REFRESH_TOKEN:
          serviceImpl.createRefreshToken((org.example.grpc.CreateRefreshTokenRequest) request,
              (io.grpc.stub.StreamObserver<org.example.grpc.CreateRefreshTokenResponse>) responseObserver);
          break;
        case METHODID_ROTATE_REFRESH_TOKEN:
          serviceImpl.rotateRefreshToken((org.example.grpc.RotateRefreshTokenRequest) request,
              (io.grpc.stub.StreamObserver<org.example.grpc.RotateRefreshTokenResponse>) responseObserver);
          break;
        case METHODID_REVOKE_TOKEN:
          serviceImpl.revokeToken((org.example.grpc.RevokeTokenRequest) request,
              (io.grpc.stub.StreamObserver<org.example.grpc.RevokeTokenResponse>) responseObserver);
          break;
        case METHODID_REVOKE_ALL_TOKENS_OF_USER:
          serviceImpl.revokeAllTokensOfUser((org.example.grpc.RevokeAllTokensOfUserRequest) request,
              (io.grpc.stub.StreamObserver<org.example.grpc.RevokeAllTokensOfUserResponse>) responseObserver);
          break;
        case METHODID_IS_VALID:
          serviceImpl.isValid((org.example.grpc.IsValidRequest) request,
              (io.grpc.stub.StreamObserver<org.example.grpc.IsValidResponse>) responseObserver);
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
          getCreateRefreshTokenMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              org.example.grpc.CreateRefreshTokenRequest,
              org.example.grpc.CreateRefreshTokenResponse>(
                service, METHODID_CREATE_REFRESH_TOKEN)))
        .addMethod(
          getRotateRefreshTokenMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              org.example.grpc.RotateRefreshTokenRequest,
              org.example.grpc.RotateRefreshTokenResponse>(
                service, METHODID_ROTATE_REFRESH_TOKEN)))
        .addMethod(
          getRevokeTokenMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              org.example.grpc.RevokeTokenRequest,
              org.example.grpc.RevokeTokenResponse>(
                service, METHODID_REVOKE_TOKEN)))
        .addMethod(
          getRevokeAllTokensOfUserMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              org.example.grpc.RevokeAllTokensOfUserRequest,
              org.example.grpc.RevokeAllTokensOfUserResponse>(
                service, METHODID_REVOKE_ALL_TOKENS_OF_USER)))
        .addMethod(
          getIsValidMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              org.example.grpc.IsValidRequest,
              org.example.grpc.IsValidResponse>(
                service, METHODID_IS_VALID)))
        .build();
  }

  private static abstract class RefreshTokenServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    RefreshTokenServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return org.example.grpc.TokenServiceProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("RefreshTokenService");
    }
  }

  private static final class RefreshTokenServiceFileDescriptorSupplier
      extends RefreshTokenServiceBaseDescriptorSupplier {
    RefreshTokenServiceFileDescriptorSupplier() {}
  }

  private static final class RefreshTokenServiceMethodDescriptorSupplier
      extends RefreshTokenServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    RefreshTokenServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (RefreshTokenServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new RefreshTokenServiceFileDescriptorSupplier())
              .addMethod(getCreateRefreshTokenMethod())
              .addMethod(getRotateRefreshTokenMethod())
              .addMethod(getRevokeTokenMethod())
              .addMethod(getRevokeAllTokensOfUserMethod())
              .addMethod(getIsValidMethod())
              .build();
        }
      }
    }
    return result;
  }
}
