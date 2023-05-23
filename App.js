import { StyleSheet, Text, View, TouchableOpacity, NativeModules, Alert, TextInput} from 'react-native'
import React, {useState} from 'react'
import DateTimePicker from '@react-native-community/datetimepicker';

const App = () => {
  
  const [hora1, sethora1] = useState(0);
  const [min1, setmin1] = useState(0);
  const [hora2, sethora2] = useState(0);
  const [min2, setmin2] = useState(0);
  const [selectedDate, setSelectedDate] = useState(new Date());
  const {CustomModule} = NativeModules;
  const [showPicker, setshowPicker] = useState(false)
  const confirmTarea = () => {
    return(
      Alert.alert(
        'Programar Alarmas.',
        `Alarma1 -> ${hora1}:${min1} --- Alarma2 -> ${hora2}:${min2}`,
        [
          {
            text: 'Cancelar',
            style: 'cancel',
            onPress: () => {
              console.log('cancelar...');
            },
          },
          {
            text: 'Aceptar',
            onPress: () => {
              programarTarea();
            },
          },
        ],
        { cancelable: false }
      )
    )
  }

  const programarTarea = async () => {
    try {
      const result = await CustomModule.scheduleAlarm(hora1,min1,hora2, min2);
      console.log(` ===== ${result}`);
      alert("Se inicio la ejecución en segundo plano se realizará una petición al api. ")
    } catch (e) {
      console.error(e);
    }
  }
  
  const cancelarTarea = async () => {
    try {
      const result = await CustomModule.cancelAlarm();
      console.log(` ===== ${result}`);
      alert("Se cancelo la alarma.")
    } catch (e) {
      console.error(e);
    }
  }

  const validarEntero = (texto, setValue) => {
    // Intentar convertir el texto en un entero
    var numero = parseInt(texto);
  
    // Comprobar si el resultado es un número válido y si es un entero
    if (!isNaN(numero) && Number.isInteger(numero)) {
      setValue(numero)
    } else {
      alert("El texto no es un número entero menor de");
    }
  }

  return (
    <View style={styles.body}>
      <Text>App test de tarea segundo plano</Text>
      <Text>con AlarmManager...</Text>
      <View>
          <Text style={styles.title}>Hora de la alarma 1:</Text>
          <View style={styles.fila} >
            <TextInput
              placeholder='Hora 24hr'
              keyboardType='number-pad'
              onChangeText={(text)=>validarEntero(text,sethora1)}
              style = {styles.input}
            />
            <TextInput
              placeholder='Min'
              keyboardType='number-pad'
              onChangeText={(text)=>validarEntero(text,setmin1)}
              style = {styles.input}
            />
          </View>
          <Text style={styles.title}>Hora de la alarma 2:</Text>
          <View style={styles.fila} >
            <TextInput
              placeholder='Hora 24hr'
              keyboardType='number-pad'
              onChangeText={(text)=>validarEntero(text,sethora2)}
              style = {styles.input}
            />
            <TextInput
              placeholder='Min'
              keyboardType='number-pad'
              onChangeText={(text)=>validarEntero(text,setmin2)}
              style = {styles.input}
            />
          </View>
      </View>
      <View style={styles.container}>
        <TouchableOpacity style={styles.buttonContainer} 
          onPress={() => confirmTarea()}>
          <Text style={styles.buttonText}>Programar Tarea...</Text>
        </TouchableOpacity>
      </View>
      <View style={styles.container}>
        <TouchableOpacity style={styles.buttonContainerCancel} 
          onPress={() => cancelarTarea()}>
          <Text style={styles.buttonText}>Cancelar Tarea...</Text>
        </TouchableOpacity>
      </View>
    </View>
  )
}

export default App

const styles = StyleSheet.create({
  body: {
    height: '100%',
    width: '100%',
    alignItems: 'center',
    justifyContent: 'center'
  },
  container: {
    margin:5
  },
  buttonContainer: {
    backgroundColor: 'blue',
    borderRadius: 10,
    padding: 10,
    width: 200,
  },
  buttonContainerCancel: {
    backgroundColor: 'red',
    borderRadius: 10,
    padding: 10,
    width: 200,
  },
  buttonText: {
    color: 'white',
    textAlign: 'center',
  },
  input: {
    borderColor: 'blue',
    borderWidth: 1,
    margin: 5,
    borderRadius: 10,
    width: 80,
    padding: 10
  },
  inputHora: {
    borderColor: 'blue',
    borderWidth: 1,
    margin: 5,
    borderRadius: 10,
    height: 50,
    width: 200,
    alignItems: 'center',
    justifyContent: 'center'
  },
  title:{
    margin: 5
  },
  fila: {
    flexDirection: 'row'
  }
})
