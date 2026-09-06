type IconType = "success" | "loading" | "error" | "none" | "fail" | "exception";

const message = {
  success: (success: string, icon: IconType = "success", mask: boolean = false) => {
    uni.showToast({
      title: success,
      icon: icon ? icon : "success",
      mask: mask,
      position: "bottom"
    });
  },
  error: function (error: string, icon: IconType = "none", mask: boolean = false) {
    uni.showToast({
      title: error,
      icon: icon ? icon : "none",
      mask: mask,
      position: "bottom"
    });
  }
};

export default message;
