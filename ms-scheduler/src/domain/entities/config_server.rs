use std::collections::HashMap;
use serde::{Deserialize, Serialize};

#[derive(Debug, Deserialize, Serialize)]
pub struct Config {
    pub name: String,
    pub profiles: Vec<String>,
    pub label: Option<String>,
    pub version: String,
    pub state: Option<String>,
    #[serde(rename = "propertySources")]
    pub property_sources: Vec<PropertySource>,
}

#[derive(Debug, Deserialize, Serialize)]
pub struct PropertySource {
    pub name: String,
    pub source: HashMap<String, String>,
}