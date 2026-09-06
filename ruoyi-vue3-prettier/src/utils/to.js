/**
 * @param { Promise } promise
 * @param { Object= } errorExt - Additional Information you can pass to the err object
 * @return { Promise }
 */
function to(promise, errorExt) {
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
