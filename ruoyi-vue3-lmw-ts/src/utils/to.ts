function to<T = any>(promise: Promise<T>, errorExt?: anyObj) {
  return promise
    .then((data) => [data, null])
    .catch((err) => {
      if (errorExt) {
        const parsedError = Object.assign({}, err, errorExt)
        return [void 0, parsedError]
      }
      return [void 0, err]
    })
}

export default to
