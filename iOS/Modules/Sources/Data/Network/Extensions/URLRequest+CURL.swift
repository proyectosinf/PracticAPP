import Foundation

extension URLRequest {
    func curl(session: URLSession? = nil) -> String {
        var curlCommand = "curl"
        curlCommand += " -X \(httpMethod ?? "GET")"
        curlCommand += " '\(url?.absoluteString ?? "")'"

        var headers = allHTTPHeaderFields ?? [:]

        if let sessionHeaders = session?.configuration.httpAdditionalHeaders as? [String: String] {
            sessionHeaders.forEach { headers[$0.key] = $0.value }
        }

        for (key, value) in headers.sorted(by: { $0.key < $1.key }) {
            curlCommand += " -H '\(key): \(value)'"
        }

        if let bodyData = httpBody, let body = String(bytes: bodyData, encoding: .utf8) {
            let escapedBody = body.replacingOccurrences(of: "'", with: "\\'")
            curlCommand += " -d '\(escapedBody)'"
        }

        return curlCommand
    }
}
