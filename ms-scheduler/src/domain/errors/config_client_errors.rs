#[derive(Debug)]
pub enum GetConfigError {
    HttpError(String),
    ConfigServerError(String),
    DeserializationFailed(String),
}

impl From<reqwest::Error> for GetConfigError {
    fn from(error: reqwest::Error) -> Self {
        GetConfigError::HttpError(format!("Request error: {}", error))
    }
}