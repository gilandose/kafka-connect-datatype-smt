package com.gilandose.kafka.connect.transforms

import java.util

import org.apache.kafka.common.config.{AbstractConfig, ConfigDef}
import org.apache.kafka.common.utils.Utils
import org.apache.kafka.connect.connector.ConnectRecord
import org.apache.kafka.connect.data.Schema
import org.apache.kafka.connect.storage.Converter

/**
  * The SQL transformer. It takes two sql entries for keys and values.
  *
  * @tparam T Record type
  */
class DataTypeTransformation[T <: ConnectRecord[T]] extends org.apache.kafka.connect.transforms.Transformation[T] {
  private var converter: Converter = _
  override def apply(record: T): T = {
    val topic = record.topic()
    val convertedBytes = converter.fromConnectData(record.topic(), record.valueSchema(), record.value())
    record.newRecord(
      record.topic(),
      record.kafkaPartition(),
      record.keySchema(),
      record.key(),
      Schema.OPTIONAL_BYTES_SCHEMA,
      convertedBytes,
      record.timestamp()
    )
  }

  override def config(): ConfigDef = Transformation.configDef

  override def close(): Unit = {}

  override def configure(configs: util.Map[String, _]): Unit = {
    val config = new TransformationConfig(configs)
    val prefix = "value.converter"
    converter = Utils.newInstance(config.getString(prefix), classOf[Converter])
    converter.configure(config.originalsWithPrefix(prefix), false)

  }
}

private object DataTypeTransformation {

  val KEY_SQL_CONFIG = "connect.transforms.sql.key"
  private val KEY_SQL_DOC =
    "Provides the SQL transformation for the keys. To provide more than one separate them by ';'"
  private val KEY_SQL_DISPLAY = "Key(-s) SQL transformation"

  val VALUE_CONVERTER = "value.converter"
  private val VALUE_CONVERTER_DOC =
    "Provides the SQL transformation for the Kafka message value. To provide more than one separate them by ';'"
  private val VALUE_CONVERTER_DISPLAY = "Value(-s) converter transformation"

  val configDef: ConfigDef = new ConfigDef()
    .define(
      VALUE_CONVERTER,
      ConfigDef.Type.CLASS,
      null,
      ConfigDef.Importance.MEDIUM,
      VALUE_CONVERTER_DOC,
      "Transforms",
      1,
      ConfigDef.Width.MEDIUM,
      VALUE_CONVERTER_DISPLAY
    )
}

class DataTypeTransformationConfig(config: util.Map[String, _]) extends AbstractConfig(Transformation.configDef, config)
