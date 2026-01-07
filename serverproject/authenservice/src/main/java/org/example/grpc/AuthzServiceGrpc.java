package org.example.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.59.0)",
    comments = "Source: authzmw.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class AuthzServiceGrpc {

  private AuthzServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "authz.AuthzService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<org.example.grpc.CheckPermissionRequest,
      org.example.grpc.CheckPermissionResponse> getCheckPermissionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CheckPermission",
      requestType = org.example.grpc.CheckPermissionRequest.class,
      responseType = org.example.grpc.CheckPermissionResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<org.example.grpc.CheckPermissionRequest,
      org.example.grpc.CheckPermissionResponse> getCheckPermissionMethod() {
    io.grpc.MethodDescriptor<org.example.grpc.CheckPermissionRequest, org.example.grpc.CheckPermissionResponse> getCheckPermissionMethod;
    if ((getCheckPermissionMethod = AuthzServiceGrpc.getCheckPermissionMethod) == null) {
      synchronized (AuthzServiceGrpc.class) {
        if ((getCheckPermissionMethod = AuthzServiceGrpc.getCheckPermissionMethod) == null) {
          AuthzServiceGrpc.getCheckPermissionMethod = getCheckPermissionMethod =
              io.grpc.MethodDescriptor.<org.example.grpc.CheckPermissionRequest, org.example.grpc.CheckPermissionResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CheckPermission"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.example.grpc.CheckPermissionRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  org.example.grpc.CheckPermissionResponse.getDefaultInstance()))
              .setSchemaDescriptor(new AuthzServiceMethodDescriptorSupplier("CheckPermission"))
              .build();
        }
      }
    }
    return getCheckPermissionMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static AuthzServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AuthzServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AuthzServiceStub>() {
        @java.lang.Override
        public AuthzServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AuthzServiceStub(channel, callOptions);
        }
      };
    return AuthzServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static AuthzServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AuthzServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AuthzServiceBlockingStub>() {
        @java.lang.Override
        public AuthzServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AuthzServiceBlockingStub(channel, callOptions);
        }
      };
    return AuthzServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static AuthzServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AuthzServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AuthzServiceFutureStub>() {
        @java.lang.Override
        public AuthzServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AuthzServiceFutureStub(channel, callOptions);
        }
      };
    return AuthzServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void checkPermission(org.example.grpc.CheckPermissionRequest request,
        io.grpc.stub.StreamObserver<org.example.grpc.CheckPermissionResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCheckPermissionMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service AuthzService.
   */
  public static abstract class AuthzServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return AuthzServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service AuthzService.
   */
  public static final class AuthzServiceStub
      extends io.grpc.stub.AbstractAsyncStub<AuthzServiceStub> {
    private AuthzServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AuthzServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AuthzServiceStub(channel, callOptions);
    }

    /**
     */
    public void checkPermission(org.example.grpc.CheckPermissionRequest request,
        io.grpc.stub.StreamObserver<org.example.grpc.CheckPermissionResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCheckPermissionMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service AuthzService.
   */
  public static final class AuthzServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<AuthzServiceBlockingStub> {
    private AuthzServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AuthzServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AuthzServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public org.example.grpc.CheckPermissionResponse checkPermission(org.example.grpc.CheckPermissionRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCheckPermissionMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service AuthzService.
   */
  public static final class AuthzServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<AuthzServiceFutureStub> {
    private AuthzServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AuthzServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AuthzServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<org.example.grpc.CheckPermissionResponse> checkPermission(
        org.example.grpc.CheckPermissionRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCheckPermissionMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_CHECK_PERMISSION = 0;

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
        case METHODID_CHECK_PERMISSION:
          serviceImpl.checkPermission((org.example.grpc.CheckPermissionRequest) request,
              (io.grpc.stub.StreamObserver<org.example.grpc.CheckPermissionResponse>) responseObserver);
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
          getCheckPermissionMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              org.example.grpc.CheckPermissionRequest,
              org.example.grpc.CheckPermissionResponse>(
                service, METHODID_CHECK_PERMISSION)))
        .build();
  }

  private static abstract class AuthzServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    AuthzServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return org.example.grpc.Authzmw.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("AuthzService");
    }
  }

  private static final class AuthzServiceFileDescriptorSupplier
      extends AuthzServiceBaseDescriptorSupplier {
    AuthzServiceFileDescriptorSupplier() {}
  }

  private static final class AuthzServiceMethodDescriptorSupplier
      extends AuthzServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    AuthzServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (AuthzServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new AuthzServiceFileDescriptorSupplier())
              .addMethod(getCheckPermissionMethod())
              .build();
        }
      }
    }
    return result;
  }
}
